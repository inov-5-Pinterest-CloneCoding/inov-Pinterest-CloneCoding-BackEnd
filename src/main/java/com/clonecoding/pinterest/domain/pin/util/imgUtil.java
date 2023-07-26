package com.clonecoding.pinterest.domain.pin.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class imgUtil {

    private static final Tika tika = new Tika();


    public static boolean validImgFile(InputStream inputStream) throws IOException {
        try {
            List<String> ValidTypeList = Arrays.asList("image/jpeg", "image/png", "image/gif", "image/bmp", "image/x-windows-bmp");

            String mimeType = tika.detect(inputStream);
            log.info("MimeType : " + mimeType);

            boolean isValid = ValidTypeList.stream().anyMatch(notValidType -> notValidType.equalsIgnoreCase(mimeType));

            return isValid;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
