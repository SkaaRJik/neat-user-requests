package ru.filippov.neat.samba;

import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
public class SambaWorker {

    // Путь к сетевой папке с которой будем работать
//    private final String NETWORK_FOLDER;
//    private final NtlmPasswordAuthentication AUTH;
    private final String URL;
    private final String USERNAME;
    private final String PASSWORD;
    private final String SHARED_DIRECTORY;
    private final String FOLDER_FOR_SOURCE_FILES;
    private final String FOLDER_FOR_VERIFIED_FILES;
    private final String FOLDER_FOR_NORMALIZED_FILES;
    private final String FOLDER_FOR_REPORT_FILES;


    public SambaWorker(String url,
                       String sharedDirectory,
                       String userName,
                       String password,
                       String folderForSourceFiles,
                       String folderForVerifiedFiles,
                       String folderForNormalizedFiles,
                       String folderForReportFiles) throws IOException {
//        this.NETWORK_FOLDER = String.format("smb://%s/%s/", url, sharedDirectory);
        this.URL = url;
        this.USERNAME = userName;
        this.PASSWORD = password;
        this.SHARED_DIRECTORY = sharedDirectory;
        //this.AUTH = new NtlmPasswordAuthentication("", userName, password);
        this.FOLDER_FOR_SOURCE_FILES = folderForSourceFiles;
        this.FOLDER_FOR_VERIFIED_FILES = folderForVerifiedFiles;
        this.FOLDER_FOR_NORMALIZED_FILES = folderForNormalizedFiles;
        this.FOLDER_FOR_REPORT_FILES = folderForReportFiles;
        this.handshake();
    }

    private void handshake() throws IOException {

        SMBClient client = new SMBClient();
        try (Connection connection = client.connect(URL)) {
            AuthenticationContext ac = new AuthenticationContext(USERNAME, PASSWORD.toCharArray(), "");
            Session session = connection.authenticate(ac);

            // Connect to Share
            DiskShare share = (DiskShare) session.connectShare(SHARED_DIRECTORY);
            /*for (FileIdBothDirectoryInformation f : share.list("", "*")) {
                System.out.println("File : " + f.getFileName());
            }*/
        } catch (Exception ex) {
            log.error("SambaWorker.handshake", ex);
        }


        /*SmbFile file = new SmbFile(NETWORK_FOLDER, AUTH);
        if (!file.canRead()) {
            throw new IOException(String.format("SAMBA: Can't read the folder: %s", NETWORK_FOLDER));
        }
        *//*if(!file.canWrite()){
            throw new IOException(String.format("SAMBA: Can't write into the folder: %s", NETWORK_FOLDER));
        }*/

    }

    // Читаем
    private byte[] readFile(String fileName) throws IOException {
        /*StringBuilder stringBuilder = new StringBuilder(NETWORK_FOLDER);
        stringBuilder.append(fileName);
        // Ресолвим путь назначения в SmbFile
        SmbFile file = new SmbFile(stringBuilder.toString(), AUTH);

        byte[] fileBytes = null;

        if (file.canRead()) {
            file.connect();
            BufferedInputStream in = new BufferedInputStream(new SmbFileInputStream(file));
            fileBytes = in.readAllBytes();
        } else {
            throw new IOException(String.format("SAMBA: Can't read the file: %s", stringBuilder.toString()));
        }
        return fileBytes;*/
        return null;

    }

    //Записываем
    private boolean writeBytesArray(String filePathToSave, byte[] bytes) throws IOException {
        /*StringBuilder stringBuilder = new StringBuilder(NETWORK_FOLDER);
        stringBuilder.append(filePathToSave);

        SmbFileOutputStream destFileName = new SmbFileOutputStream(
                new SmbFile(stringBuilder.toString(), AUTH));

        destFileName.write(bytes);
        destFileName.flush();*/
        /*InputStream streamToWrite = new ByteArrayInputStream(bytes);
        // Ну и копируем все из исходного потока в поток назначения.
        BufferedReader brl = new BufferedReader(
                new InputStreamReader(streamToWrite));
        String b = null;
        while((b=brl.readLine())!=null){
            destFileName.write(b.getBytes());
        }
        destFileName.flush();*/


        return true;
    }

    private String writeFileToNetworkFolder(MultipartFile file, String projectName, String username, String networkFileName) throws IOException {
        String filePath = String.format("%s/%s/%s.xlsx", username, networkFileName, projectName);
        if(!this.writeBytesArray(filePath, file.getBytes())){
            throw new IOException(String.format("File: %s was not saved", filePath));
        }
        return filePath;
    }

    public String writeSourceFile(MultipartFile file, String projectName, String username) throws IOException {
        return writeFileToNetworkFolder(file, projectName, username, this.FOLDER_FOR_SOURCE_FILES);
    }


}
