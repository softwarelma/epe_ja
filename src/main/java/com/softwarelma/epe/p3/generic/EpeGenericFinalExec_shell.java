package com.softwarelma.epe.p3.generic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;
import com.softwarelma.epe.p3.print.EpePrintFinalPrint_os_name;

public final class EpeGenericFinalExec_shell extends EpeGenericAbstract {

	private static final String PASSWORD_MASK = "*********";

	@Override
	public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
		String postMessage = "exec_shell, expected no param or str (contaning the shell script).";
		String shellScript = getStringAt(listExecResult, 0, postMessage, null);
		List<String> listPassword = getListStringAt(listExecResult, 1, postMessage, new ArrayList<>());
		String exit = execShellScript(shellScript, listPassword);
		log(execParams, exit);
		return createResult(exit);
	}

	public static String execShellScript(String shellScript, List<String> listPassword) throws EpeAppException {
		return execShellScript(shellScript, listPassword, new ArrayList<>());
	}

	public static String execShellScript(String shellScript, List<String> listPassword, List<String> listFinalOutput) throws EpeAppException {
		if (shellScript == null || shellScript.trim().isEmpty())
			return execShellCommands(null, listPassword, listFinalOutput);
		List<String> listCommands = new ArrayList<>();
		String[] arrayCommands = shellScript.split("\n");
		for (String command : arrayCommands)
			listCommands.add(command);
		return execShellCommands(listCommands, listPassword, listFinalOutput);
	}

	public static String execShellCommands(List<String> listCommands, List<String> listPassword) throws EpeAppException {
		return execShellCommands(listCommands, listPassword, new ArrayList<>());
	}

	public static String execShellCommands(List<String> listCommands, List<String> listPassword, List<String> listFinalOutput) throws EpeAppException {
		listCommands = listCommands == null ? new ArrayList<>() : listCommands;
		try {
			ProcessBuilder builder = retrieveProcessBuilder();
			builder.redirectErrorStream(true); // so we can ignore the error stream
			Process process = builder.start();
			InputStream out = process.getInputStream();
			OutputStream in = process.getOutputStream();
			byte[] buffer = new byte[4000];
			boolean first = true;
			String lastExecuted = null;
			while (isAlive(process)) {
				int no = out.available();
				if (no > 0) {
					String output = readAll(process, out, buffer);
					if (output.contains("\n")) {
						String typed = output.substring(0, output.indexOf("\n"));
						String consoleOutput = output.substring(typed.length() + 1);
						String forFile = lastExecuted == null ? output : lastExecuted + "\n" + consoleOutput;
						forFile = cleanPasswords(forFile, listPassword);
						String forConsole = first ? output : consoleOutput;
						forConsole = cleanPasswords(forConsole, listPassword);
						// only file:
						EpeAppLogger.log(forFile, null, null, false, true, false);
						// only console:
						EpeAppLogger.logSystemOutPrint(forConsole);
						listFinalOutput.add(forConsole);
						first = false;
					} else {
						output = cleanPasswords(output, listPassword);
						// file and console
						EpeAppLogger.log(output, null, null, true, true, false);
						listFinalOutput.add(output);
					}
				}
				if (listCommands.isEmpty()) {
					int ni = System.in.available();
					if (ni > 0) {
						int n = System.in.read(buffer, 0, Math.min(ni, buffer.length));
						String input = new String(buffer, 0, n);
						lastExecuted = input.substring(0, input.length() - 1);
						if (input.startsWith("exit")) {
							// only file:
							EpeAppLogger.log("exit", null, null, false, true);
						}
						in.write(buffer, 0, n);
						in.flush();
					}
				} else {
					String input = listCommands.remove(0);
					lastExecuted = input;
					input += "\n";
					String forConsole = cleanPasswords(input, listPassword);
					// only console:
					EpeAppLogger.logSystemOutPrint(forConsole);
					if (input.startsWith("exit")) {
						// only file:
						EpeAppLogger.log("exit", null, null, false, true);
					}
					in.write(input.getBytes());
					in.flush();
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
				}
			}
			String exit = process.exitValue() + "";
			// EpeAppLogger.log(typed, null, null, false, true);
			// System.out.println(exit);
			// EpeAppLogger.log(exit);
			EpeAppLogger.log(exit, null, null, true, true, false);
			return exit;
		} catch (IOException e) {
			throw new EpeAppException("exec_shell", e);
		}
	}

	private static ProcessBuilder retrieveProcessBuilder() throws EpeAppException {
		if (EpeGenericFinalIs_windows.isWindows()) {
			return new ProcessBuilder("CMD");
		} else if (EpeGenericFinalIs_unix.isUnix()) {
			return new ProcessBuilder("bash", "-i");
		} else if (EpeGenericFinalIs_mac.isMac()) {
			return new ProcessBuilder("bash", "-i");
		} else if (EpeGenericFinalIs_solaris.isSolaris()) {
			return new ProcessBuilder("bash", "-i");
		} else {
			throw new EpeAppException("Unknown operating system: " + EpePrintFinalPrint_os_name.retrieveOsName());
		}
	}

	private static boolean isAlive(Process p) {
		try {
			p.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}

	private static String readAll(Process process, InputStream out, byte[] buffer) throws EpeAppException {
		try {
			StringBuilder sb = new StringBuilder();
			while (isAlive(process)) {
				int no = out.available();
				if (no > 0) {
					// buffer = new byte[4000];
					int n = out.read(buffer, 0, Math.min(no, buffer.length));
					String output = new String(buffer, 0, n);
					output = cleanForConsole(output);
					sb.append(output);
				} else {
					break;
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
				}
			}
			return sb.toString();
		} catch (IOException e) {
			throw new EpeAppException("exec_shell", e);
		}
	}

	private static String cleanPasswords(String str, List<String> listPassword) {
		for (String password : listPassword)
			str = str.replace(password, PASSWORD_MASK);
		return str;
	}

	private static String cleanForConsole(String str) {
		return str.replace("\u0008", "\u0020");
	}

}
