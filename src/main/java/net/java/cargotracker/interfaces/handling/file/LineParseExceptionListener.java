package net.java.cargotracker.interfaces.handling.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.chunk.listener.SkipReadListener;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Dependent
@Named("LineParseExceptionListener")
public class LineParseExceptionListener implements SkipReadListener {

    private static final String FAILED_DIRECTORY = "failed_directory";

    private static final Logger logger = Logger.getLogger(LineParseExceptionListener.class.getName());

    @Autowired
    private JobContext jobContext;

    @Override
    public void onSkipReadItem(Exception e) throws Exception {
        File failedDirectory = new File(jobContext.getProperties().getProperty(FAILED_DIRECTORY));
        if (!failedDirectory.exists()) {
            failedDirectory.mkdirs();
        }
        EventLineParseException parseException = (EventLineParseException) e;
        logger.log(Level.WARNING, "Problem parsing event file line", parseException);
        try (PrintWriter failed = new PrintWriter(new BufferedWriter(new FileWriter(new File(failedDirectory, "failed_" + jobContext.getJobName() + "_" + jobContext.getInstanceId() + ".csv"), true)))) {
            failed.println(parseException.getLine());
        }
    }
}
