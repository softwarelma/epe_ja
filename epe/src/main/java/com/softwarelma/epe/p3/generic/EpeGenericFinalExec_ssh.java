package com.softwarelma.epe.p3.generic;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeGenericFinalExec_ssh extends EpeGenericAbstract {

    /**
     * see also https://kodehelp.com/java-program-for-uploading-file-to-sftp-server/
     */
    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "exec_ssh, expected the text.";
        String text = getStringAt(listExecResult, 0, postMessage);

        // TODO

        String str = text;
        log(execParams, str);
        return createResult(str);
    }

    public static void execSsh() throws EpeAppException {
        // TODO
        String SFTPHOST = "10.20.30.40";
        int SFTPPORT = 22;
        String SFTPUSER = "username";
        String SFTPPASS = "password";
        String SFTPWORKINGDIR = "/export/home/kodehelp/";
        String FILETOTRANSFER = "file";

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(SFTPWORKINGDIR);
            File f = new File(FILETOTRANSFER);
            channelSftp.put(new FileInputStream(f), f.getName());
        } catch (Exception e) {
            throw new EpeAppException("exec_ssh", e);
        }
    }

}
