package com.example;

import com.example.domain.models.Sale;
import com.example.utils.RandomString;
import org.joda.time.DateTime;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Created by Todd Nash on 10/29/16.
 */
public class StoreTestUtil {
    public static Sale createModel(Long id, DateTime saleDate) {

        Sale model = Sale.getBuilder(saleDate)
                .build();

        ReflectionTestUtils.setField(model, "id", id);

        return model;
    }

    public static String createStringWithLength(int length) {
        RandomString randomString = new RandomString(length);
        return randomString.nextString();
    }
}


