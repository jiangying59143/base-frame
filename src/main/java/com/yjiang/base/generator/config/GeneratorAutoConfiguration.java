package com.yjiang.base.generator.config;

import com.yjiang.base.generator.db.DbInfoInitializer;
import com.yjiang.base.generator.modular.controller.CodeController;
import cn.stylefeng.roses.core.db.DbInitializer;
import com.yjiang.base.generator.modular.service.TableService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 代码生成器的自动配置
 *
 * @author fengshuonan
 * @date 2018-10-09-下午3:27
 */
@Configuration
public class GeneratorAutoConfiguration {

    @Bean
    @Lazy
    public CodeController codeController() {
        return new CodeController();
    }

    @Bean
    public TableService tableService() {
        return new TableService();
    }

    @Bean
    public DbInitializer dbInitializer() {
        return new DbInfoInitializer();
    }
}
