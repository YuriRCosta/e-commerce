package br.com.ecommerce.ecommerce;

import br.com.ecommerce.ecommerce.model.dto.ObjetoErro;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.postgresql.util.PSQLException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.List;

@RestControllerAdvice
public class ControleExcecoes extends ResponseEntityExceptionHandler {

    private final String SETA = " --> ";

    @ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus statusCode, WebRequest request) {
        StringBuilder sb = new StringBuilder();

        if(ex instanceof MethodArgumentNotValidException) {
            List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
            for (ObjectError objectError : list) {
                sb.append(objectError.getDefaultMessage()).append("/n");
            }
        } else {
            sb.append(ex.getMessage());
        }

        ObjetoErro objetoErro = new ObjetoErro();
        objetoErro.setError(sb.toString());
        objetoErro.setCode(statusCode != null ? statusCode.value() + SETA + statusCode.getReasonPhrase() : HttpStatus.INTERNAL_SERVER_ERROR.value() + SETA + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return new ResponseEntity<>(objetoErro, headers, statusCode != null ? statusCode : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, PSQLException.class, SQLException.class})
    protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex) {
        String errorMessage = ex.getMessage();

        if (ex instanceof DataIntegrityViolationException || ex instanceof ConstraintViolationException ||
                ex instanceof PSQLException || ex instanceof SQLException) {
            errorMessage = ex.getCause().getCause().getMessage();
        }

        ObjetoErro objetoErro = new ObjetoErro();
        objetoErro.setError(errorMessage);
        objetoErro.setCode(HttpStatus.INTERNAL_SERVER_ERROR + SETA + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return new ResponseEntity<>(objetoErro, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
