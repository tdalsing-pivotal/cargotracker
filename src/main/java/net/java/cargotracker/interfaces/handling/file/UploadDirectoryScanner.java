package net.java.cargotracker.interfaces.handling.file;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Schedule;
import org.springframework.stereotype.Service;

/**
 * Periodically scans a certain directory for files and attempts to parse
 * handling event registrations from the contents by calling a batch job.
 * <p/>
 * Files that fail to parse are moved into a separate directory, successful
 * files are deleted.
 */
@Service
public class UploadDirectoryScanner {

    // Runs every fifteen minutes
    @Schedule(minute = "*/2", hour = "*")
    public void processFiles() {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        jobOperator.start("EventFilesProcessorJob", null);
    }
}
