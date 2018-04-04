package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.softwarelma.epe.p1.app.EpeAppConstants;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.encodings.EpeEncodings;
import com.softwarelma.epe.p2.encodings.EpeEncodingsResponse;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;
import com.softwarelma.epe.p3.disk.EpeDiskModelDir.MODEL_TYPE;

public final class EpeDiskFinalDminus extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        String postMessage = "dminus params should be 4: the first operand dir, the second "
                + "operand dir, the destination dir and the operation mode (" + EpeAppConstants.OPERATION_MODE_NAME
                + ", " + EpeAppConstants.OPERATION_MODE_CONTENT + ")";

        if (listExecResult.size() != 4) {
            throw new EpeAppException(postMessage);
        }

        // FIRST OPERAND DIR

        String firstDirStr = getStringAt(listExecResult, 0, postMessage);
        firstDirStr = EpeAppUtils.cleanFilename(firstDirStr);
        File firstDirFile = new File(firstDirStr);

        if (!firstDirFile.exists()) {
            throw new EpeAppException("dminus \"" + firstDirStr + "\" does not exist");
        }

        if (!firstDirFile.isDirectory()) {
            throw new EpeAppException("dminus \"" + firstDirStr + "\" is not a dir");
        }

        // SECOND OPERAND DIR

        String secondDirStr = getStringAt(listExecResult, 1, postMessage);
        secondDirStr = EpeAppUtils.cleanFilename(secondDirStr);
        File secondDirFile = new File(secondDirStr);

        if (!secondDirFile.exists()) {
            throw new EpeAppException("dminus \"" + secondDirStr + "\" does not exist");
        }

        if (!secondDirFile.isDirectory()) {
            throw new EpeAppException("dminus \"" + secondDirStr + "\" is not a dir");
        }

        // DESTINATION DIR

        String destinationDirStr = getStringAt(listExecResult, 2, postMessage);
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

        String operationModeStr = getStringAt(listExecResult, 3, postMessage);
        EpeAppUtils.checkContains(
                Arrays.asList(
                        new String[] { EpeAppConstants.OPERATION_MODE_NAME, EpeAppConstants.OPERATION_MODE_CONTENT }),
                "operation mode", operationModeStr);

        // DMINUS OPERATION

        EpeDiskModelDir modelDir = this.retrieveFilesToCopy(firstDirFile, secondDirFile, operationModeStr,
                destinationDirStr);

        if (execParams.getGlobalParams().isPrintToConsole()) {
            EpeAppLogger.log("Files to copy:\n" + modelDir);
        }

        destinationDirStr = destinationDirStr.endsWith("/") ? destinationDirStr : destinationDirStr + "/";
        this.copyListFileToDestination(modelDir, destinationDirStr, destinationDirStr, firstDirStr);
        return createEmptyResult();
    }

    private void copyListFileToDestination(EpeDiskModelDir modelDir, String destinationDirStrOri,
            String destinationDirStr, String firstDirStr) throws EpeAppException {
        destinationDirStr = destinationDirStr.endsWith("/") ? destinationDirStr : destinationDirStr + "/";

        for (int i = 0; i < modelDir.size(); i++) {
            EpeDiskModelFileDir modelFileDir = modelDir.get(i);
            String destinationFileName = getDestinationFileName(destinationDirStrOri, destinationDirStr, firstDirStr,
                    modelFileDir);

            if (modelFileDir.isDir()) {
                File dir = new File(destinationFileName);

                if (!dir.exists()) {
                    dir.mkdir();
                    this.copyListFileToDestination(modelFileDir.toDir(), destinationDirStrOri, destinationFileName,
                            firstDirStr);
                }
            } else {
                try {
                    FileUtils.copyFile(new File(modelFileDir.getLocation() + modelFileDir.getName()),
                            new File(destinationFileName));
                } catch (IOException e) {
                    throw new EpeAppException("dminus copying from \"" + modelFileDir.getLocation()
                            + modelFileDir.getName() + "\" to \"" + destinationFileName + "\"", e);
                }
            }
        }
    }

    private String getDestinationFileName(String destinationDirStrOri, String destinationDirStr, String firstDirStr,
            EpeDiskModelFileDir modelFileDir) {
        String intermediatePath = "";

        if (destinationDirStrOri.equals(destinationDirStr)) {
            intermediatePath = modelFileDir.getLocation().substring(firstDirStr.length());
            intermediatePath = intermediatePath.startsWith("/") ? intermediatePath.substring(1) : intermediatePath;
            intermediatePath = intermediatePath.endsWith("/") ? intermediatePath : intermediatePath + "/";
            intermediatePath = intermediatePath.length() == 1 ? "" : intermediatePath;
        }

        return destinationDirStr + intermediatePath + modelFileDir.getName();
    }

    private EpeDiskModelDir retrieveFilesToCopy(File firstDirFile, File secondDirFile, String operationModeStr,
            String destinationDirStr) throws EpeAppException {
        Map.Entry<String, String> filePathAndName = EpeAppUtils.retrieveFilePathAndName(destinationDirStr);
        EpeDiskModelDir modelDir = new EpeDiskModelDir(filePathAndName.getKey(), filePathAndName.getValue());
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
                EpeEncodings enc = new EpeEncodings();
                EpeEncodingsResponse response = enc.readGuessing(firstFile.getPath());
                String firstContent = response.getFileContent();

                step = "reading file \"" + secondFile.getPath() + "\"";
                response = enc.readGuessing(secondFile.getPath());
                String secondContent = response.getFileContent();

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
            Map.Entry<String, String> filePathAndName = EpeAppUtils.retrieveFilePathAndName(firstFileI.getPath());

            if (new File(firstFileI.getPath()).isFile()) {
                filePathAndName = EpeAppUtils.retrieveFilePathAndName(filePathAndName.getKey());
            }

            EpeDiskModelDir modelDirI = this.retrieveOrCreateModelDir(modelDir, filePathAndName.getKey(),
                    filePathAndName.getValue());
            this.retrieveFilesToCopy2(firstFileI, secondFileI, operationModeStr, modelDirI);

            if (modelDirI.size() > 0) {
                this.addToModelIfNotExists(modelDir, modelDirI);
            }
        }
    }

    private EpeDiskModelDir retrieveOrCreateModelDir(EpeDiskModelDir modelDir, String location, String name)
            throws EpeAppException {
        EpeDiskModelDir modelDirRet = (EpeDiskModelDir) modelDir.retrieveModelFileDir(location, name, MODEL_TYPE.dir);
        modelDirRet = modelDirRet == null ? new EpeDiskModelDir(location, name) : modelDirRet;
        return modelDirRet;
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
        Map.Entry<String, String> filePathAndName = EpeAppUtils.retrieveFilePathAndName(file.getPath());
        EpeDiskModelFileDir modelFileDir;

        if (file.isDirectory()) {
            modelFileDir = new EpeDiskModelDir(filePathAndName.getKey(), filePathAndName.getValue());

            if (deep) {
                for (File fileI : file.listFiles()) {
                    this.addFileToModelDir(modelFileDir.toDir(), fileI, deep);
                }
            }
        } else if (file.isFile()) {
            modelFileDir = new EpeDiskModelFile(filePathAndName.getKey(), filePathAndName.getValue());
        } else {
            throw new EpeAppException("file \"" + file.getPath() + "\" is neither a directory nor a normal file");
        }

        this.addToModelIfNotExists(modelDir, modelFileDir);
    }

    private void addToModelIfNotExists(EpeDiskModelDir modelDir, EpeDiskModelFileDir modelFileDir)
            throws EpeAppException {
        EpeDiskModelFileDir modelFileDirToFind = modelDir.retrieveModelFileDir(modelFileDir.getLocation(),
                modelFileDir.getName(), MODEL_TYPE.fileDir);

        if (modelFileDirToFind == null) {
            modelDir.add(modelFileDir);
        }
    }

}
