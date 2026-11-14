package edu.pss.spring.config;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Definición y secuencia del Job de ficheros. */
@Configuration
public class ConfigJob {

    @Bean
    public Job jobGestionFichero(
            JobRepository jobRepository,
            @Qualifier("comprobarFichero") Step comprobarFichero,
            @Qualifier("crearDirectorioSalida") Step crearDirectorioSalida,
            @Qualifier("procesarFichero") Step procesarFichero,
            @Qualifier("archivarFichero") Step archivarFichero,
            @Qualifier("listenerInfoJob") JobExecutionListener listenerInfoJob) {

        return new JobBuilder("jobGestionFichero", jobRepository)
                .start(comprobarFichero)
                .next(crearDirectorioSalida)
                .next(procesarFichero)
                .next(archivarFichero)
                .listener(listenerInfoJob)
                .build();
    }
}
