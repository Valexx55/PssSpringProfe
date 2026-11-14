package edu.pss.spring.config;

import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Listeners de seguimiento del Job y de sus Steps. */
@Configuration
public class ConfigListeners {

@Bean
public JobExecutionListener listenerInfoJob() {

    return new JobExecutionListener() {

        @Override
        public void beforeJob(JobExecution jobExecution) {

            System.out.println("\n===== INICIO DEL JOB =====");

            System.out.println("Job: "
                    + jobExecution.getJobInstance().getJobName());

            System.out.println("ID ejecución: "
                    + jobExecution.getId());

            System.out.println("Estado: "
                    + jobExecution.getStatus());
        }

        @Override
        public void afterJob(JobExecution jobExecution) {

            System.out.println("\n===== RESUMEN FINAL DEL JOB =====");

            System.out.println("Job: "
                    + jobExecution.getJobInstance().getJobName());

            System.out.println("ID ejecución: "
                    + jobExecution.getId());

            System.out.println("Estado final: "
                    + jobExecution.getStatus());

            // Contexto compartido del Job
            ExecutionContext jobContext =
                    jobExecution.getExecutionContext();
            

            System.out.println("Steps iniciados: "
                    + jobContext.getInt("stepsIniciados", 0));
            
            
            System.out.println("Último Step: "
                    + jobContext.getString("ultimoStep", "NINGUNO"));

            System.out.println("\n--- JOB EXECUTION CONTEXT ---");

            jobContext.entrySet().forEach(entrada ->
                System.out.println(
                    entrada.getKey() + " = " + entrada.getValue()
                )
            );

            // Resumen de las ejecuciones de los Steps
            System.out.println("\n--- STEPS EJECUTADOS ---");

            for (StepExecution step : jobExecution.getStepExecutions()) {

                System.out.println("\nStep: "
                        + step.getStepName());

                System.out.println("ID: " + step.getId());
                System.out.println("Estado: " + step.getStatus());

                System.out.println("Leídos: " + step.getReadCount());
                System.out.println("Escritos: " + step.getWriteCount());
                System.out.println("Filtrados: " + step.getFilterCount());

                // Contexto propio de cada Step
                ExecutionContext stepContext =
                        step.getExecutionContext();

                stepContext.entrySet().forEach(entrada ->
                    System.out.println(
                        entrada.getKey() + " = " + entrada.getValue()
                    )
                );
                
                
            }

            System.out.println("\n===== FIN DEL RESUMEN =====");
        }
    };
}

	
	@Bean
	public StepExecutionListener listenerInfoStep() {

	    return new StepExecutionListener() {

	        @Override
	        public void beforeStep(StepExecution stepExecution) {
	            System.out.println("=== INICIO DEL STEP ===");
	            String resumenPre = stepExecution.getSummary();
	            System.out.println(resumenPre);
	            
	            
	            ExecutionContext jobContext =
	                    stepExecution.getJobExecution().getExecutionContext();
	            
	            int stepsIniciados =
	                    jobContext.getInt("stepsIniciados", 0);

	            // Lo incrementamos
	            jobContext.putInt("stepsIniciados", stepsIniciados + 1);

	            // Guardamos el nombre del Step que acaba de comenzar
	            jobContext.putString(
	                    "ultimoStep",
	                    stepExecution.getStepName()
	            );
	        }

	        @Override
	        public org.springframework.batch.core.ExitStatus
	                afterStep(StepExecution stepExecution) {

	            System.out.println("=== FIN DEL STEP ===");
	            String resumenPost = stepExecution.getSummary();
	            System.out.println(resumenPost);
	            
	            // Recuperamos el contexto de ejecución del Step
	            ExecutionContext context = stepExecution.getExecutionContext();

	            System.out.println("===== EXECUTION CONTEXT =====");

	            System.out.println("Registros leídos: " +
	                    context.getInt("registrosLeidos", 0));

	            System.out.println("Registros correctos: " +
	                    context.getInt("registrosCorrectos", 0));

	            System.out.println("Registros incorrectos: " +
	                    context.getInt("registrosIncorrectos", 0));

	            stepExecution.getFailureExceptions()
	                    .forEach(e ->
	                        System.out.println("ERROR: " + e.getMessage())
	                    );

	            return null;
	        }
	    };
	}
}
