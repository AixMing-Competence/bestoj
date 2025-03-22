/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponseBoolean } from '../models/BaseResponseBoolean';
import type { BaseResponseLong } from '../models/BaseResponseLong';
import type { BaseResponsePageQuestionSubmit } from '../models/BaseResponsePageQuestionSubmit';
import type { BaseResponsePageQuestionSubmitVO } from '../models/BaseResponsePageQuestionSubmitVO';
import type { BaseResponseQuestionSubmitVO } from '../models/BaseResponseQuestionSubmitVO';
import type { DeleteRequest } from '../models/DeleteRequest';
import type { QuestionSubmitAddRequest } from '../models/QuestionSubmitAddRequest';
import type { QuestionSubmitEditRequest } from '../models/QuestionSubmitEditRequest';
import type { QuestionSubmitQueryRequest } from '../models/QuestionSubmitQueryRequest';
import type { QuestionSubmitUpdateRequest } from '../models/QuestionSubmitUpdateRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class QuestionSubmitControllerService {
    /**
     * @param requestBody
     * @returns BaseResponseBoolean OK
     * @throws ApiError
     */
    public static updateQuestionSubmit(
        requestBody: QuestionSubmitUpdateRequest,
    ): CancelablePromise<BaseResponseBoolean> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/questionsubmit/update',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * @param requestBody
     * @returns BaseResponsePageQuestionSubmitVO OK
     * @throws ApiError
     */
    public static listMyQuestionSubmitVoByPage(
        requestBody: QuestionSubmitQueryRequest,
    ): CancelablePromise<BaseResponsePageQuestionSubmitVO> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/questionsubmit/my/list/page/vo',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * @param requestBody
     * @returns BaseResponsePageQuestionSubmit OK
     * @throws ApiError
     */
    public static listQuestionSubmitByPage(
        requestBody: QuestionSubmitQueryRequest,
    ): CancelablePromise<BaseResponsePageQuestionSubmit> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/questionsubmit/list/page',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * @param requestBody
     * @returns BaseResponsePageQuestionSubmitVO OK
     * @throws ApiError
     */
    public static listQuestionSubmitVoByPage(
        requestBody: QuestionSubmitQueryRequest,
    ): CancelablePromise<BaseResponsePageQuestionSubmitVO> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/questionsubmit/list/page/vo',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * @param requestBody
     * @returns BaseResponseBoolean OK
     * @throws ApiError
     */
    public static editQuestionSubmit(
        requestBody: QuestionSubmitEditRequest,
    ): CancelablePromise<BaseResponseBoolean> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/questionsubmit/edit',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * @param requestBody
     * @returns BaseResponseBoolean OK
     * @throws ApiError
     */
    public static deleteQuestionSubmit(
        requestBody: DeleteRequest,
    ): CancelablePromise<BaseResponseBoolean> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/questionsubmit/delete',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * @param requestBody
     * @returns BaseResponseLong OK
     * @throws ApiError
     */
    public static addQuestionSubmit(
        requestBody: QuestionSubmitAddRequest,
    ): CancelablePromise<BaseResponseLong> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/questionsubmit/add',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * @param requestBody
     * @returns BaseResponseLong OK
     * @throws ApiError
     */
    public static doQuestionSubmit(
        requestBody: QuestionSubmitAddRequest,
    ): CancelablePromise<BaseResponseLong> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/questionsubmit/',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * @param id
     * @returns BaseResponseQuestionSubmitVO OK
     * @throws ApiError
     */
    public static getQuestionSubmitVoById(
        id: number,
    ): CancelablePromise<BaseResponseQuestionSubmitVO> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/questionsubmit/get/vo',
            query: {
                'id': id,
            },
        });
    }
}
