package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.softwarelma.epe.p1.app.EpeAppConstants;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDiskFinalDminus extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);
        EpeAppUtils.checkNull("listExecResult", listExecResult);

        if (listExecResult.size() != 4) {
            throw new EpeAppException("dminus params should be 4: the first operand dir, the second "
                    + "operand dir, the destination dir and the operation mode (" + EpeAppConstants.OPERATION_MODE_NAME
                    + ", " + EpeAppConstants.OPERATION_MODE_CONTENT + ")");
        }

        // FIRST OPERAND DIR

        EpeExecResult result = listExecResult.get(0);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent firstDir = result.getExecContent();
        EpeAppUtils.checkNull("firstDir", firstDir);
        String firstDirStr = firstDir.getStr();
        EpeAppUtils.checkNull("firstDirStr", firstDirStr);
        firstDirStr = EpeAppUtils.cleanFilename(firstDirStr);
        File firstDirFile = new File(firstDirStr);

        if (!firstDirFile.exists()) {
            throw new EpeAppException("dminus \"" + firstDirStr + "\" does not exist");
        }

        if (!firstDirFile.isDirectory()) {
            throw new EpeAppException("dminus \"" + firstDirStr + "\" is not a dir");
        }

        // SECOND OPERAND DIR

        result = listExecResult.get(1);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent secondDir = result.getExecContent();
        EpeAppUtils.checkNull("secondDir", secondDir);
        String secondDirStr = secondDir.getStr();
        EpeAppUtils.checkNull("secondDirStr", secondDirStr);
        secondDirStr = EpeAppUtils.cleanFilename(secondDirStr);
        File secondDirFile = new File(secondDirStr);

        if (!secondDirFile.exists()) {
            throw new EpeAppException("dminus \"" + secondDirStr + "\" does not exist");
        }

        if (!secondDirFile.isDirectory()) {
            throw new EpeAppException("dminus \"" + secondDirStr + "\" is not a dir");
        }

        // DESTINATION DIR

        result = listExecResult.get(2);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent destinationDir = result.getExecContent();
        EpeAppUtils.checkNull("destinationDir", destinationDir);
        String destinationDirStr = destinationDir.getStr();
        EpeAppUtils.checkNull("destinationDirStr", destinationDirStr);
        destinationDirStr = EpeAppUtils.cleanFilename(destinationDirStr);
        File destinationDirFile = new File(destinationDirStr);

        if (destinationDirFile.exists()) {
            if (!destinationDirFile.isDirectory()) {
                throw new EpeAppException("dminus \"" + destinationDirStr + "\" is not a dir");
            }
        } else {
            destinationDirFile.mkdirs();
        }

        // OPERATION MODE (NAME, CONTENT)

        result = listExecResult.get(3);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent operationMode = result.getExecContent();
        EpeAppUtils.checkNull("operationMode", operationMode);
        String operationModeStr = operationMode.getStr();
        EpeAppUtils.checkNull("operationModeStr", operationModeStr);
        EpeAppUtils.checkContains(
                Arrays.asList(
                        new String[] { EpeAppConstants.OPERATION_MODE_NAME, EpeAppConstants.OPERATION_MODE_CONTENT }),
                "operation mode", operationModeStr);

        // DMINUS OPERATION

        EpeDiskModelDir modelDir = this.retrieveFilesToCopy(firstDirFile, secondDirFile, operationModeStr,
                destinationDirStr);

        if (execParams.getGlobalParams().isPrintToConsole()) {
            EpeAppLogger.logSystemOutPrintln("Files to copy:\n" + modelDir);
        }

        this.copyListFileToDestination(modelDir, destinationDirStr);

        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(new EpeExecContent(null));
        return execResult;
    }

    private void copyListFileToDestination(EpeDiskModelDir modelDir, String destinationDirStr) throws EpeAppException {
        destinationDirStr = destinationDirStr.endsWith("/") ? destinationDirStr : destinationDirStr + "/";

        for (int i = 0; i < modelDir.size(); i++) {
            EpeDiskModelFileDir modelFileDir = modelDir.get(i);

            if (modelFileDir.isDir()) {
                File dir = new File(destinationDirStr + modelFileDir.getName());

                if (!dir.exists()) {
                    dir.mkdir();
                    this.copyListFileToDestination(modelFileDir.toDir(), destinationDirStr + modelFileDir.getName());
                }
            } else {
                try {
                    FileUtils.copyFile(new File(modelFileDir.getLocation() + modelFileDir.getName()),
                            new File(destinationDirStr + modelFileDir.getName()));
                } catch (IOException e) {
                    throw new EpeAppException("dminus copying from \"" + modelFileDir.getLocation()
                            + modelFileDir.getName() + "\" to \"" + destinationDirStr + modelFileDir.getName() + "\"",
                            e);
                }
            }
        }
    }

    private EpeDiskModelDir retrieveFilesToCopy(File firstDirFile, File secondDirFile, String operationModeStr,
            String destinationDirStr) throws EpeAppException {
        String[] locationAndName = EpeAppUtils.retrieveLocationAndName(destinationDirStr);
        EpeDiskModelDir modelDir = new EpeDiskModelDir(locationAndName[0], locationAndName[1]);
        File[] arraySecondFile = secondDirFile.listFiles();

        for (File firstFileI : firstDirFile.listFiles()) {
            File secondFileI = this.retrieveFileByName(arraySecondFile, firstFileI.getName());
            this.retrieveFilesToCopy2(firstFileI, secondFileI, operationModeStr, modelDir);
        }

        return modelDir;
    }

    private void retrieveFilesToCopy2(File firstFile, File secondFile, String operationModeStr,
            EpeDiskModelDir modelDir) throws EpeAppException {
        // SECOND FILE NOT FOUND

        if (secondFile == null) {
            this.addFileToModelDir(modelDir, firstFile, true);
            return;
        }

        String step = null;

        if (!firstFile.getName().equals(secondFile.getName())) {
            throw new EpeAppException(
                    "file names does not match: \"" + firstFile.getName() + "\", \"" + secondFile.getName() + "\"");
        }

        // NAMES ARE THE SAME

        if (firstFile.isFile() && secondFile.isFile()) {
            // BOTH ARE FILES

            if (operationModeStr.equals(EpeAppConstants.OPERATION_MODE_NAME)) {
                return;
            }

            try {
                step = "reading file \"" + firstFile.getPath() + "\"";
                String firstContent = EpeAppUtils.readFileAsStringGuessingEncoding(firstFile.getPath());
                step = "reading file \"" + secondFile.getPath() + "\"";
                String secondContent = EpeAppUtils.readFileAsStringGuessingEncoding(secondFile.getPath());

                if (!firstContent.equals(secondContent)) {
                    this.addFileToModelDir(modelDir, firstFile, false);
                }

                return;
            } catch (Exception e) {
                throw new EpeAppException(step, e);
            }
        }

        // ONE IS A FILE AND THE OTHER IS A DIR

        if (!firstFile.isDirectory() || !secondFile.isDirectory()) {
            this.addFileToModelDir(modelDir, firstFile, false);
            return;
        }

        // BOTH ARE DIRS

        File[] arrayFirstFile = firstFile.listFiles();
        File[] arraySecondFile = secondFile.listFiles();

        for (File firstFileI : arrayFirstFile) {
            File secondFileI = this.retrieveFileByName(arraySecondFile, firstFileI.getName());
            String[] locationAndName = EpeAppUtils.retrieveLocationAndName(firstFileI.getPath());
            EpeDiskModelDir modelDirI = new EpeDiskModelDir(locationAndName[0], locationAndName[1]);
            this.retrieveFilesToCopy2(firstFileI, secondFileI, operationModeStr, modelDirI);

            if (modelDirI.size() > 0) {
                modelDir.add(modelDirI);
            }
        }
    }

    private File retrieveFileByName(File[] arrayFile, String name) {
        for (File file : arrayFile) {
            if (file.getName().equals(name)) {
                return file;
            }
        }

        return null;
    }

    private void addFileToModelDir(EpeDiskModelDir modelDir, File file, boolean deep) throws EpeAppException {
        String[] locationAndName = EpeAppUtils.retrieveLocationAndName(file.getPath());
        EpeDiskModelFileDir modelFileDir;

        if (file.isDirectory()) {
            modelFileDir = new EpeDiskModelDir(locationAndName[0], locationAndName[1]);

            if (deep) {
                for (File fileI : file.listFiles()) {
                    this.addFileToModelDir(modelFileDir.toDir(), fileI, deep);
                }
            }
        } else if (file.isFile()) {
            modelFileDir = new EpeDiskModelFile(locationAndName[0], locationAndName[1]);
        } else {
            throw new EpeAppException("file \"" + file.getPath() + "\" is neither a directory nor a normal file");
        }

        modelDir.add(modelFileDir);
    }

}
