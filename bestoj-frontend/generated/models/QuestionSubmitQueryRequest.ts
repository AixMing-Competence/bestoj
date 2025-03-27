/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { JudgeInfo } from './JudgeInfo';
export type QuestionSubmitQueryRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    id?: number;
    language?: string;
    code?: string;
    judgeInfo?: JudgeInfo;
    status?: number;
    questionId?: number;
    userId?: number;
    createTime?: string;
    notId?: number;
    searchText?: string;
};

