package com.softwarelma.epe.p3.print;

import java.text.NumberFormat;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpePrintFinalPrint_mem_usage extends EpePrintAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String str = this.retrieveMemoryUsage(null);
        log(execParams, str);
        return createResult(str);
    }

    /**
     * in MB
     * 
     * from: http://stackoverflow.com/questions/74674/how-to-do-i-check-cpu-and-memory -usage-in-java
     */
    private String retrieveMemoryUsage(String title) throws EpeAppException {
        Runtime runtime = Runtime.getRuntime();
        NumberFormat format = NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder("MEMORY USAGE");
        sb.append(title == null ? "" : " - " + title);
        sb.append("\n");
        long max = runtime.maxMemory();
        long total = runtime.totalMemory();
        long free = runtime.freeMemory();
        long used = total - free;

        // B -> MB
        sb.append("\tmax memory (from OS): " + format.format(max / 1024 / 1024) + " MB\n");
        // B -> MB
        sb.append("\ttotal memory (allocated): " + format.format(total / 1024 / 1024) + " MB\n");
        // B -> MB
        sb.append("\tfree memory (from total): " + format.format(free / 1024 / 1024) + " MB\n");
        // B -> MB
        sb.append("\tused memory (from total): " + format.format(used / 1024 / 1024) + " MB");

        return sb.toString();
    }

}
