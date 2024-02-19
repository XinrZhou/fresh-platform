package com.example.product.exception;


import com.example.common.exception.Code;
import com.example.common.exception.XException;
import com.example.common.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.r2dbc.UncategorizedR2dbcException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(XException.class)
    public Mono<ResultVO> handleValidException(XException exception) {
        return Mono.just(ResultVO.error(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResultVO> handleException(Exception exception) {
        System.out.println("test===" + exception.getMessage());
        return Mono.just(ResultVO.error(400, "请求错误"));
    }

    @ExceptionHandler(UncategorizedR2dbcException.class)
    public Mono<ResultVO> UncategorizedR2dbcException(UncategorizedR2dbcException exception) {
        return Mono.just(ResultVO.error(Code.BAD_REQUEST.getCode(), "唯一约束冲突！" + exception.getMessage()));
    }
}
