package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public class EpeDiskFinalFdzip extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);
        EpeAppUtils.checkEmptyList("listExecResult", listExecResult);
        EpeAppUtils.checkRange(listExecResult.size(), 2, Integer.MAX_VALUE, false, true,
                "fdzip, indicate at least the zip file and the file to zip.");

        EpeExecResult result = listExecResult.get(0);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent zip = result.getExecContent();
        EpeAppUtils.checkNull("zip", zip);
        String zipStr = zip.getStr();
        EpeAppUtils.checkNull("zipStr", zipStr);

        List<String> listFullFileName = new ArrayList<>();

        for (int i = 1; i < listExecResult.size(); i++) {
            result = listExecResult.get(i);
            EpeAppUtils.checkNull("result", result);
            EpeExecContent fileToZip = result.getExecContent();
            EpeAppUtils.checkNull("fileToZip", fileToZip);
            String fileToZipStr = fileToZip.getStr();
            EpeAppUtils.checkNull("fileToZipStr", fileToZipStr);
            listFullFileName.add(fileToZipStr);
        }

        this.createZip(execParams.getGlobalParams().isPrintToConsole(), listFullFileName, zipStr);
        return this.createEmptyResult();
    }

    private void createZip(boolean doLog, List<String> listFullFileName, String zipFileName) throws EpeAppException {
        ZipOutputStream zos = null;
        String currentFullFileName = "";

        try {
            FileOutputStream fos = new FileOutputStream(zipFileName);
            zos = new ZipOutputStream(fos);

            if (doLog) {
                EpeAppLogger.log("zip file: \"" + zipFileName + "\"");
            }

            for (String fullFileName : listFullFileName) {
                currentFullFileName = fullFileName;
                addZipEntryFileOrFolder(doLog, "", fullFileName, zos);
            }
        } catch (IOException e) {
            throw new EpeAppException("createZip() - \"" + currentFullFileName + "\"", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    throw new EpeAppException("createZip() - \"" + currentFullFileName + "\"", e);
                }
            }
        }
    }

    private void addZipEntryFileOrFolder(boolean doLog, String zipFolder, String fullFileName, ZipOutputStream zos)
            throws IOException, EpeAppException {
        fullFileName = fullFileName.replace("\\", "/");
        File file = new File(fullFileName);

        if (file.isDirectory()) {
            String[] arrayFile = file.list();
            fullFileName += fullFileName.endsWith("/") ? "" : "/";
            Map.Entry<String, String> filePathAndName = EpeAppUtils.retrieveFilePathAndName(fullFileName);

            for (String fileName : arrayFile) {
                addZipEntryFileOrFolder(doLog, zipFolder + filePathAndName.getValue() + "/", fullFileName + fileName,
                        zos);
            }
        } else {
            addZipEntryFile(doLog, zipFolder, fullFileName, zos);
        }
    }

    private void addZipEntryFile(boolean doLog, String zipFolder, String fullFileName, ZipOutputStream zos)
            throws IOException, EpeAppException {
        byte[] buffer = new byte[1024];
        Map.Entry<String, String> filePathAndName = EpeAppUtils.retrieveFilePathAndName(fullFileName);
        ZipEntry ze = new ZipEntry(zipFolder + filePathAndName.getValue());
        zos.putNextEntry(ze);
        FileInputStream in = new FileInputStream(fullFileName);
        int len;

        while ((len = in.read(buffer)) > 0) {
            zos.write(buffer, 0, len);
        }

        in.close();
        zos.closeEntry();

        if (doLog) {
            EpeAppLogger.log("\tzipping file: \"" + fullFileName + "\"");
            EpeAppLogger.log("\t\tinto: \"" + zipFolder + "\"");
        }
    }

}
