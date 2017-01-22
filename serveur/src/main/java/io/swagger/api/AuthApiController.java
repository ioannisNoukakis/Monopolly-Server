package io.swagger.api;

import com.cristallium.api.AuthApi;

import com.cristallium.api.dto.Token;
import com.cristallium.api.dto.User;
import io.swagger.annotations.*;

import io.swagger.database.dao.UserRepository;
import io.swagger.utils.JWTutils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class AuthApiController implements AuthApi {

    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseEntity<Token> authPost(@ApiParam(value = "users to be connected" ,required=true ) @RequestBody User user) {

        io.swagger.database.model.User userDB = userRepository.findByUsername(user.getUsername());
        if(userDB == null) //dosen't exists
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(!userDB.getPassword().equals(user.getPassword()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Token token = new Token();
        token.setToken(JWTutils.createToken(""+userDB.getId()));
        token.setUserId(userDB.getId());
        return new ResponseEntity<Token>(token, HttpStatus.OK);
    }
}
