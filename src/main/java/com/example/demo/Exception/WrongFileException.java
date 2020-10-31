package com.example.demo.Exception;

public class WrongFileException extends RuntimeException{
    public WrongFileException(){
        super("Your File uploaded is not in correct format!");
    }
}
