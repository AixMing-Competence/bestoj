package com.aixming.bestojcodesandbox.utils;

import cn.hutool.core.util.StrUtil;
import com.aixming.bestojcodesandbox.model.ExecuteMessage;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 进程工具类
 *
 * @author AixMing
 * @since 2025-03-20 15:55:03
 */
public class ProcessUtil {

    public static void main(String[] args) {
        try {
            Process process = Runtime.getRuntime().exec("cmd");
            OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
            writer.write("dir");
            writer.write("\n");
            writer.flush();

            writer.write("exit");
            writer.write("\n");
            writer.flush();

            process.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            writer.close();
            reader.close();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行进程并获取信息
     *
     * @param process
     * @param operation
     * @return
     */
    public static ExecuteMessage runProcessAndGetMessage(Process process, String operation) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            // 等待程序执行，获取状态码
            int exitCode = process.waitFor();
            executeMessage.setExitCode(exitCode);
            if (exitCode == 0) {
                System.out.println(operation + "成功");
                // 打印输出信息
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder compileOutputStringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    compileOutputStringBuilder.append(line);
                }
                executeMessage.setMessage(compileOutputStringBuilder.toString());
                reader.close();
            } else {
                // 编译失败
                System.out.println(operation + "失败");
                // 打印输出信息
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder compileOutputStringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    compileOutputStringBuilder.append(line);
                }
                executeMessage.setMessage(compileOutputStringBuilder.toString());
                // 获取错误信息
                BufferedReader errorOutputReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorOutputStringBuilder = new StringBuilder();
                while ((line = errorOutputReader.readLine()) != null) {
                    errorOutputStringBuilder.append(line);
                }
                executeMessage.setErrorMessage(errorOutputStringBuilder.toString());

                reader.close();
                errorOutputReader.close();
                process.destroy();
            }
            stopWatch.stop();
            executeMessage.setTime(stopWatch.getTotalTimeMillis());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return executeMessage;
    }

    /**
     * 执行交互式进程并获取信息
     *
     * @param process
     * @param args
     * @return
     */
    public static ExecuteMessage runInteractProcessAndGetMessage(Process process, String args) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            // 向进程输入命令
            String[] argsSplit = args.split(" ");
            // 插入回车键，代表执行当前命令
            String join = StrUtil.join("\n", argsSplit) + "\n";
            OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
            writer.write(join);
            writer.flush();
            // 等待程序执行，获取状态码
            int exitCode = process.waitFor();
            executeMessage.setExitCode(exitCode);
            if (exitCode == 0) {
                System.out.println("执行成功");
                // 打印输出信息
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder compileOutputStringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    compileOutputStringBuilder.append(line);
                }
                executeMessage.setMessage(compileOutputStringBuilder.toString());
                reader.close();
            } else {
                // 编译失败
                System.out.println("执行失败");
                // 打印输出信息
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder compileOutputStringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    compileOutputStringBuilder.append(line);
                }
                executeMessage.setMessage(compileOutputStringBuilder.toString());
                // 获取错误信息
                BufferedReader errorOutputReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorOutputStringBuilder = new StringBuilder();
                while ((line = errorOutputReader.readLine()) != null) {
                    errorOutputStringBuilder.append(line);
                }
                executeMessage.setErrorMessage(errorOutputStringBuilder.toString());

                reader.close();
                errorOutputReader.close();
                process.destroy();
            }
            stopWatch.stop();
            executeMessage.setTime(stopWatch.getTotalTimeMillis());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return executeMessage;
    }
}
