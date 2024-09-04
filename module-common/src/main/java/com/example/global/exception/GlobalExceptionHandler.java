package com.example.global.exception;

import com.example.global.exception.custom.SocialLoginException;
import com.example.global.exception.custom.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Controller 내에서 발생하는 Exception 대해서 Catch 하여 응답값(Response)을 보내주는 기능을 수행함.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;

    /**
     * [Exception] 잘못된 서버 요청일 경우 발생한 경우
     * 400 -> 문법상 오류가 있어서 서버가 요청 사항을 이해하지 못할때 발생되는 에러
     * @param e HttpClientErrorException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<ErrorResponse> handleBadRequestException(HttpClientErrorException e) {
        log.error("HttpClientErrorException.BadRequest", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.BAD_REQUEST_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e){
        log.error("handleDataIntegrityViolationException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.DUPLICATE_REQUEST_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    /** 비밀번호 틀렸을 때 */
    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ErrorResponse> handleUserNotFoundException(BadCredentialsException e){
        log.error("BadCredentialsException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.BAD_REQUEST_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    /** 소셜 로그인 실패 */
    @ExceptionHandler(SocialLoginException.class)
    protected ResponseEntity<ErrorResponse> SocialLoginException(SocialLoginException e){
        log.error("SocialLoginException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.FORBIDDEN_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e){
        log.error("UserNotFoundException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_FOUND_USER_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    @ExceptionHandler(SignatureException.class)
    protected ResponseEntity<ErrorResponse> handleSignatureException (SignatureException e) {
        log.error("SignatureException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.WRONG_TYPE_TOKEN, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    @ExceptionHandler(MalformedJwtException.class)
    protected ResponseEntity<ErrorResponse> handleMalformedJwtException (MalformedJwtException e) {
        log.error("MalformedJwtException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.UNKNOWN_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<ErrorResponse> handleExpiredJwtException (ExpiredJwtException e) {
        log.error("ExpiredJwtException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.EXPIRED_TOKEN, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    protected ResponseEntity<ErrorResponse> handleUnsupportedJwtException (UnsupportedJwtException e) {
        log.error("UnsupportedJwtException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.UNSUPPORTED_TOKEN, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException (IllegalArgumentException e) {
        log.error("IllegalArgumentException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.ACCESS_DENIED, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }



    @ExceptionHandler(EmptyResultDataAccessException.class)
    protected ResponseEntity<ErrorResponse> handleEmptyResultDataAccessException (EmptyResultDataAccessException e) {
        log.error("EmptyResultDataAccessException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NO_DATA, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    /**
     * [Exception] API 호출 시 '객체' 혹은 '파라미터' 데이터 값이 유효하지 않은 경우
     *
     * @param ex MethodArgumentNotValidException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("handleMethodArgumentNotValidException", ex);
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField()).append(":");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(", ");
        }
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_VALID_ERROR, String.valueOf(stringBuilder));
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }


//    /**
//     * [Exception] API 호출 시 'Header' 내에 데이터 값이 유효하지 않은 경우
//     *
//     * @param ex MissingRequestHeaderException
//     * @return ResponseEntity<ErrorResponse>
//     */
//    @ExceptionHandler(MissingRequestHeaderException.class)
//    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
//        log.error("MissingRequestHeaderException", ex);
//        final ErrorResponse response = ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
//        return new ResponseEntity<>(response, HTTP_STATUS_OK);
//    }




    /**
     * [Exception] 잘못된 주소로 요청 한 경우
     *
     * @param e NoHandlerFoundException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoHandlerFoundExceptionException(NoHandlerFoundException e) {
        log.error("handleNoHandlerFoundExceptionException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_FOUND_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }


//    /**
//     * [Exception] NULL 값이 발생한 경우
//     *
//     * @param e NullPointerException
//     * @return ResponseEntity<ErrorResponse>
//     */
//    @ExceptionHandler(NullPointerException.class)
//    protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
//        log.error("handleNullPointerException", e);
//        final ErrorResponse response = ErrorResponse.of(ErrorCode.NULL_POINT_ERROR, e.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
//    }

//    /**
//     * Input / Output 내에서 발생한 경우
//     *
//     * @param ex IOException
//     * @return ResponseEntity<ErrorResponse>
//     */
//    @ExceptionHandler(IOException.class)
//    protected ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
//        log.error("IOException", ex);
//        final ErrorResponse response = ErrorResponse.of(ErrorCode.IO_ERROR, ex.getMessage());
//        return new ResponseEntity<>(response, HTTP_STATUS_OK);
//    }

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    protected ResponseEntity<ErrorResponse> handleDuplicateInfoException(DataIntegrityViolationException ex) {
//        log.error("handleDuplicateInfoException", ex);
//        final ErrorResponse response = ErrorResponse.of(ErrorCode.DUPLICATE_INFO_ERROR, ex.getMessage());
//        return new ResponseEntity<>(response, HTTP_STATUS_OK);
//    }

    // ==================================================================================================================

    /**
     * [Exception] 모든 Exception 경우 발생
     *
     * @param ex Exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(Exception.class)
    protected final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("Exception", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }
}

