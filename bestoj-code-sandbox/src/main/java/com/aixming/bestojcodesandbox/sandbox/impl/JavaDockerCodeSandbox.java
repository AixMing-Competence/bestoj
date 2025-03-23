package com.aixming.bestojcodesandbox.sandbox.impl;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.aixming.bestojcodesandbox.model.ExecuteCodeRequest;
import com.aixming.bestojcodesandbox.model.ExecuteCodeResponse;
import com.aixming.bestojcodesandbox.model.ExecuteMessage;
import com.aixming.bestojcodesandbox.model.JudgeInfo;
import com.aixming.bestojcodesandbox.sandbox.JavaCodeSandboxTemplate;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class JavaDockerCodeSandbox extends JavaCodeSandboxTemplate {

    @Resource
    private DockerClient dockerClient;

    private boolean FIRST_INIT = true;

    private final long TIME_OUT = 5000L;

    public static void main(String[] args) {
        JavaDockerCodeSandbox javaDockerCodeSandbox = new JavaDockerCodeSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setLanguage("java");
        executeCodeRequest.setInputList(Arrays.asList("11 23", "31 41"));
        String code = ResourceUtil.readUtf8Str("code/Main.java");
        executeCodeRequest.setCode(code);
        ExecuteCodeResponse executeCodeResponse = javaDockerCodeSandbox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }

    @Override
    public List<ExecuteMessage> runCode(File userCodeFile, List<String> inputList) {
        // 拉取镜像
        String image = "openjdk:17-alpine";
        // 如果是第一次才需要拉取
        if (FIRST_INIT) {
            PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
                @Override
                public void onNext(PullResponseItem item) {
                    System.out.println("下载镜像：" + item.getStatus());
                    super.onNext(item);
                }
            };
            try {
                dockerClient.pullImageCmd(image).exec(pullImageResultCallback).awaitCompletion();
            } catch (InterruptedException e) {
                System.out.println("拉取镜像异常");
                e.printStackTrace();
            }
            FIRST_INIT = false;
        }
        System.out.println("拉取镜像完成");
        // 创建容器，并将本地用户代码挂载到容器内的 /app 文件中
        String containerId = createAndCopyFileToContainer(dockerClient, image, userCodeFile);
        // 启动容器
        dockerClient.startContainerCmd(containerId).exec();
        // 运行代码，并获取输出结果
        List<ExecuteMessage> executeMessageList = runCodeInContainer(dockerClient, containerId, inputList);
        // 删除容器
        dockerClient.removeContainerCmd(containerId)
                .withForce(true)
                .exec();
        return executeMessageList;
    }

    @Override
    public ExecuteCodeResponse collectResultMessage(List<ExecuteMessage> executeMessageList) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        ArrayList<String> outputList = new ArrayList<>();
        long maxTime = 0, maxMemory = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                executeCodeResponse.setStatus(3);
                break;
            }
            Long time = executeMessage.getTime();
            Long memory = executeMessage.getMemory();
            if (time != null) {
                maxTime = Math.max(time, maxTime);
            }
            if (memory != null) {
                maxMemory = Math.max(memory, maxMemory);
            }
            outputList.add(executeMessage.getMessage());
        }
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMemory(maxMemory);
        judgeInfo.setTime(maxTime);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }

    /**
     * 在容器内运行代码，返回输出结果
     *
     * @param dockerClient
     * @param containerId
     * @param inputList
     * @return
     */
    public List<ExecuteMessage> runCodeInContainer(DockerClient dockerClient, String containerId, List<String> inputList) {
        ArrayList<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String input : inputList) {
            StringBuilder inputArgsStringBuilder = new StringBuilder(ArrayUtil.join(input.split(" "), "\n"));
            inputArgsStringBuilder.append("\n");
            String[] cmdString = ArrayUtil.append(new String[]{"java", "-cp", "/app", "Main"});
            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                    .withCmd(cmdString)
                    .withAttachStderr(true)
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .exec();
            System.out.println("创建执行命令：" + execCreateCmdResponse);
            final String[] message = {null};
            final String[] errorMessage = {null};
            long time = 0L;
            final boolean[] timeOut = {true};
            String execId = execCreateCmdResponse.getId();
            ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
                @Override
                public void onComplete() {
                    timeOut[0] = false;
                    super.onComplete();
                }

                @Override
                public void onNext(Frame frame) {
                    StreamType streamType = frame.getStreamType();
                    if (StreamType.STDERR.equals(streamType)) {
                        errorMessage[0] = new String(frame.getPayload());
                        System.out.println("输出错误结果：" + errorMessage[0]);
                    } else {
                        message[0] = new String(frame.getPayload());
                        System.out.println("输出结果：" + message[0]);
                    }
                    super.onNext(frame);
                }
            };
            final long[] maxMemory = {0L};
            // 获取占用内存
            StatsCmd statsCmd = dockerClient.statsCmd(containerId);
            ResultCallback<Statistics> statisticsResultCallback = statsCmd.exec(new ResultCallback<Statistics>() {
                @Override
                public void onNext(Statistics statistics) {
                    System.out.println("内存占用：" + statistics.getMemoryStats().getUsage());
                    maxMemory[0] = Math.max(statistics.getMemoryStats().getUsage(), maxMemory[0]);
                }

                @Override
                public void onStart(Closeable closeable) {

                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }

                @Override
                public void close() throws IOException {

                }
            });
            statsCmd.exec(statisticsResultCallback);

            try {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                dockerClient.execStartCmd(execId)
                        .withStdIn(new ByteArrayInputStream(inputArgsStringBuilder.toString().getBytes()))
                        .exec(execStartResultCallback)
                        .awaitCompletion(TIME_OUT, TimeUnit.MILLISECONDS);
                stopWatch.stop();
                time = stopWatch.getTotalTimeMillis();
                statsCmd.close();
            } catch (InterruptedException e) {
                System.out.println("程序执行异常");
                e.printStackTrace();
            }
            ExecuteMessage executeMessage = new ExecuteMessage();
            executeMessage.setMessage(message[0]);
            executeMessage.setErrorMessage(errorMessage[0]);
            executeMessage.setTime(time);
            executeMessage.setMemory(maxMemory[0]);
            executeMessageList.add(executeMessage);
        }
        return executeMessageList;
    }

    /**
     * 创建容器，并将用户代码复制到容器内部
     *
     * @param dockerClient
     * @param image
     * @param userCodeFile
     * @return
     */
    public String createAndCopyFileToContainer(DockerClient dockerClient, String image, File userCodeFile) {
        // 创建容器
        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(image);
        HostConfig hostConfig = new HostConfig();
        hostConfig.withMemory(512 * 1000 * 1000L);
        hostConfig.withCpuCount(1L);
        hostConfig.setBinds(new Bind(userCodeFile.getParent(), new Volume("/app")));
        CreateContainerResponse createContainerResponse = createContainerCmd
                .withHostConfig(hostConfig)
                .withAttachStdin(true)
                .withAttachStderr(true)
                .withAttachStdout(true)
                .withTty(true)
                .withCmd("/bin/sh")
                .exec();
        System.out.println(createContainerResponse);
        String containerId = createContainerResponse.getId();
        return containerId;
    }

}
