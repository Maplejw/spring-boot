package com.igg.boot.framework.autoconfigure.db;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;


@Configuration
@Aspect
@AutoConfigureAfter(DBDefaultAutoconfiguration.class)
@ConditionalOnBean(name = DBDefaultAutoconfiguration.PRIMARY_DATASOURCE)
@EnableConfigurationProperties(DBTransactionProperties.class)
public class DBTransactionAutoconfiguration {
	@Autowired
	private DBTransactionProperties transactionProperties;
	private static final String SPLIT_SYMBOL = ",";

	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager(
			@Qualifier(DBDefaultAutoconfiguration.PRIMARY_DATASOURCE) DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	public TransactionInterceptor txAdvice(DataSourceTransactionManager transactionManager) {
		String[] operate = transactionProperties.getMethod().split(SPLIT_SYMBOL);
		Map<String, TransactionAttribute> method = new HashMap<>(operate.length);
		for (String setMethod : operate) {
			RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
			transactionAttribute.setName(setMethod);
			transactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			transactionAttribute.setTimeout(transactionProperties.getTimeout());
			transactionAttribute
					.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
			method.put(setMethod, transactionAttribute);
		}
		RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
		transactionAttribute.setName("*");
		transactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
		transactionAttribute.setReadOnly(true);
		method.put("*", transactionAttribute);

		NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
		source.setNameMap(method);

		TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager, source);

		return txAdvice;
	}

	@Bean
	public Advisor txAdviceAdvisor(TransactionInterceptor txAdvice) {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(transactionProperties.getExpression());

		return new DefaultPointcutAdvisor(pointcut, txAdvice);
	}
}
