package com.hisense.serverestimate;

import com.google.common.base.Joiner;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.commons.codec.Charsets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.sound.midi.SysexMessage;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.security.KeyRep.Type.SECRET;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataCaptureApplicationTests {

    @Test
    public void contextLoads() {
        Date date = new Date();
        System.out.println(date);
    }

    public static void main(String[] args) {
        getDateString("1",false);
        getDateString("1",true);
    }
    public static void getDateString(String str,boolean flag){
        System.out.println(str);
        System.out.println(flag);
    }
}
