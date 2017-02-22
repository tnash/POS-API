package com.example.controllers;

import com.example.domain.models.Sale;
import com.example.services.SaleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan("com.example")
@EnableJpaRepositories(basePackages = "com.example.domain")
@EnableTransactionManagement
public class StoreApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private SaleService saleService;

    @Before
    public void setup() {
        given(this.saleService.
                get(1l)
        ).willReturn(
                new Sale());
    }

    @Test
    public void test() {
        this.restTemplate.getForEntity("/{id}",
                Sale.class, "1");
    }

}
