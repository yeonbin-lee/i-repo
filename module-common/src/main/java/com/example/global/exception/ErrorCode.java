package com.example.global.exception;

import lombok.Getter;

/**
 * [공통 코드] API 통신에 대한 '에러 코드'를 Enum 형태로 관리를 한다.
 * Global Error CodeList : 전역으로 발생하는 에러코드를 관리한다.
 * Custom Error CodeList : 업무 페이지에서 발생하는 에러코드를 관리한다
 * Error Code Constructor : 에러코드를 직접적으로 사용하기 위한 생성자를 구성한다.
 *
 * @author lee
 */
@Getter
public enum ErrorCode {

    /**
     * ******************************* Global Error CodeList ***************************************
     * HTTP Status Code
     * 400 : Bad Request
     * 401 : Unauthorized
     * 403 : Forbidden
     * 404 : Not Found
     * 500 : Internal Server Error
     * *********************************************************************************************
     */
    // 잘못된 서버 요청
    BAD_REQUEST_ERROR(400, "G001", "올바르지 않은 요청 정보"),

    DUPLICATE_REQUEST_ERROR(400, "G002", "Inserting Duplicate Data into DB"),

//    // 파라미터 정보 없음
//    PARAMETER_MISSING_ERROR(400, "G002", "파라미터 정보 없음"),

    // 권한이 없음
    FORBIDDEN_ERROR(401, "G003", "권한 없음"),

    // 서버로 요청한 리소스가 존재하지 않음
    NOT_FOUND_ERROR(404, "G004", "Not Found Exception"),

    // 유저 정보가 존재하지 않음
    NOT_FOUND_USER_ERROR(404, "G004", "Not Found User Exception"),


    NOT_VALID_ERROR(404, "G005", "handle Validation Exception"),

    UNKNOWN_ERROR(404, "G006", "토큰이 존재하지 않습니다."),
    WRONG_TYPE_TOKEN(404, "G007","변조된 토큰입니다."),
    EXPIRED_TOKEN(404, "G008" ,"만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(404,"G009" ,"변조된 토큰입니다."),
    ACCESS_DENIED(404, "G010","권한이 없습니다."),

    // 입력/출력 값이 유효하지 않음
//    IO_ERROR(400, "G006", "I/O Exception"),

    NO_DATA(404, "G011", "존재하지않는 데이터입니다."),

    DUPLICATE_INFO_ERROR(409, "G013", "Duplicated value"),

    // 서버가 처리 할 방법을 모르는 경우 발생
    INTERNAL_SERVER_ERROR(500, "G999", "Internal Server Error Exception"),

    /**
     * ******************************* Custom Error CodeList ***************************************
     */
    // Transaction Insert Error
    INSERT_ERROR(200, "9999", "Insert Transaction Error Exception"),

    // Transaction Update Error
    UPDATE_ERROR(200, "9999", "Update Transaction Error Exception"),

    // Transaction Delete Error
    DELETE_ERROR(200, "9999", "Delete Transaction Error Exception"),

    ; // End

    /**
     * ******************************* Error Code Constructor ***************************************
     */
    // 에러 코드의 '코드 상태'을 반환한다.
    private final int status;

    // 에러 코드의 '코드간 구분 값'을 반환한다.
    private final String divisionCode;

    // 에러 코드의 '코드 메시지'을 반환한다.
    private final String message;

    // 생성자 구성
    ErrorCode(final int status, final String divisionCode, final String message) {
        this.status = status;
        this.divisionCode = divisionCode;
        this.message = message;
    }
}

