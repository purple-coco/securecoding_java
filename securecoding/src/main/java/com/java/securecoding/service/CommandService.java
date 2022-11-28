package com.java.securecoding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;

@Service
@RequiredArgsConstructor
public class CommandService {

    //2. 코드 삽입
    public static String EvalService(String src) throws ScriptException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("javascript");

        return (String)scriptEngine.eval(src);

    }

    //3. 경로 조작 및 자원 삽입
    public static String filePathService(String fileName) {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            String result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            return result;


        } catch (IOException e) {
            System.out.println("error");
        }
        return null;
    }

    //4. 크로스사이트 스크립트
    public static String XSSFilterService(String input) {
        input = input.replaceAll("<", "&lt;");
        input = input.replaceAll(">", "&gt;");
        input = input.replaceAll("/", "&#47;");
        input = input.replaceAll("'", "&#39;");
        input = input.replaceAll("\"", "&#x22;");
        input = input.replaceAll("`", "&#96;");
        input = input.replaceAll("[(]", "&#x28;");
        input = input.replaceAll("[)]", "&#x29;");

        return input;
    }

    //5. 운영체제 명령어 삽입
    public static String OSCommandService(String cmd) throws IOException {

        Process process = null;

        process = Runtime.getRuntime().exec(cmd);

        try (
                InputStream inputStream = process.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        ){
            String line = null;
            String result = "";
            while((line = bufferedReader.readLine()) != null) {
                result += " "+ line;
            }
            return result;

        } catch (IOException e) {
            System.out.println("error");
        }

        return null;
    }


}
