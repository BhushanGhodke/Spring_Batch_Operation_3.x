package com.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.demo.entity.Employee;
import com.demo.repository.EmployeeRepository;

@Configuration

public class SpringBatchConfig {

	
//	private JobBuilderFactory jobBuiilderFactory;
//	private StepBuilderFactory stepBuilderFactory;


	@Autowired
	private EmployeeRepository employeeRepository;






	@Bean
	public FlatFileItemReader<Employee> employeeReader() {

		FlatFileItemReader<Employee> itemReader = new FlatFileItemReader<>();

		itemReader.setResource(new FileSystemResource("src/main/resources/employee.csv"));
		
		itemReader.setName("csv-reader");

		itemReader.setLinesToSkip(1);

		itemReader.setLineMapper(lineMapper());

		return itemReader;
	}

	private LineMapper<Employee> lineMapper() {

		DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter(",");
		tokenizer.setStrict(false);
		tokenizer.setNames("id", "name", "salary", "city", "address", "country");

		BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Employee.class);

		lineMapper.setFieldSetMapper(fieldSetMapper);
		lineMapper.setLineTokenizer(tokenizer);

		return lineMapper;
	}

	@Bean
	public EmployeeProcessor employeeProcessor() {
		return new EmployeeProcessor();
	}

	@Bean
	public RepositoryItemWriter<Employee> employeeWriter() {

		RepositoryItemWriter<Employee> writer = new RepositoryItemWriter<>();
		writer.setRepository(employeeRepository);
		writer.setMethodName("save");
		return writer;
	}

	@Bean
	public Step step(JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		return new StepBuilder("step", jobRepository)
				.<Employee, Employee>chunk(10,transactionManager)
				.reader(employeeReader())
				.processor(employeeProcessor())
				.writer(employeeWriter())
				.taskExecutor(taskExecutor())
				.build();
	}

	@Bean
	public Job job(JobRepository jobRepository , PlatformTransactionManager transactionManager) {

		return new JobBuilder("employee-import", jobRepository)
				.flow(step(jobRepository, transactionManager))
				.end()
				.build();
	}

	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.setConcurrencyLimit(10);
		return taskExecutor;
	}
}
