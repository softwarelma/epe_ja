package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public class EpeDiskFinalFdunzip extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "fdunzip, expected the zip file name and optionally the output directory.";
        String zipFileName = getStringAt(listExecResult, 0, postMessage);
        log(execParams, "zip file: \"" + zipFileName + "\"");
        String outputDirectory = getStringAt(listExecResult, 1, postMessage, "");
        unZip(execParams.getGlobalParams().isPrintToConsole(), zipFileName, outputDirectory);
        return createEmptyResult();
    }

    public static void unZip(boolean doLog, String zipFileName, String outputDirectory) throws EpeAppException {
        EpeAppUtils.checkEmpty("zipFileName", zipFileName);
        EpeAppUtils.checkNull("outputDirectory", outputDirectory);

        if (!new File(zipFileName).exists()) {
            return;
        }

        outputDirectory = EpeAppUtils.cleanDirName(outputDirectory);
        byte[] buffer = new byte[1024];

        try {
            // create output directory is not exists
            File folder = new File(outputDirectory);
            if (!folder.exists()) {
                folder.mkdir();
            }

            // get the zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFileName));
            // get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(outputDirectory + fileName);

                if (doLog) {
                    EpeAppLogger.log("\tunzipping file: \"" + newFile.getAbsoluteFile() + "\"");
                }

                if (ze.isDirectory()) {
                    newFile.mkdir();
                    ze = zis.getNextEntry();
                    continue;
                }

                if (!outputDirectory.isEmpty()) {
                    // create all non exists folders
                    // else you will hit FileNotFoundException for compressed folder
                    new File(newFile.getParent()).mkdirs();
                }

                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
