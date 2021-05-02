package ru.filippov.neat.samba;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mserref.NtStatus;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.mssmb2.SMBApiException;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.Directory;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.FileSystemException;
import java.util.HashSet;
import java.util.Set;

@Log4j2
public class SambaWorker {

    // Путь к сетевой папке с которой будем работать
//    private final String NETWORK_FOLDER;
//    private final NtlmPasswordAuthentication AUTH;

    private final String FOLDER_FOR_SOURCE_FILES;
    private final String FOLDER_FOR_VERIFIED_FILES;
    private final String FOLDER_FOR_NORMALIZED_FILES;
    private final String FOLDER_FOR_REPORT_FILES;

    private SMBClient client = null;
    private Connection connection;
    private Session session;
    private DiskShare diskShare;

    public SambaWorker(String url,
                       String sharedDirectory,
                       String userName,
                       String password,
                       String folderForSourceFiles,
                       String folderForVerifiedFiles,
                       String folderForNormalizedFiles,
                       String folderForReportFiles) throws IOException {

        this.FOLDER_FOR_SOURCE_FILES = folderForSourceFiles;
        this.FOLDER_FOR_VERIFIED_FILES = folderForVerifiedFiles;
        this.FOLDER_FOR_NORMALIZED_FILES = folderForNormalizedFiles;
        this.FOLDER_FOR_REPORT_FILES = folderForReportFiles;

        try {
            client = new SMBClient();
            connection = client.connect(url);
            if(connection.isConnected()) {
                log.info("successfully created connection to ["+connection.getRemoteHostname()+"]");
            }
            AuthenticationContext ac = new AuthenticationContext(userName, password.toCharArray(), "");
            session = connection.authenticate(ac);
            if(session == null) {
                throw new FileSystemException("Cannot create session for user ["+userName+"] on domain ["+password+"]");
            }
            diskShare = (DiskShare) session.connectShare(sharedDirectory);
            if(diskShare == null) {
                throw new FileSystemException("Cannot connect to the share ["+ sharedDirectory +"]");
            }
        } catch (IOException e) {
            throw new IOException("Cannot connect to samba server", e);
        }
    }

    public void close() throws IOException {

            if(diskShare != null) {
                diskShare.close();
            }
            if(session != null) {
                session.close();
            }
            if(connection != null) {
                connection.close();
            }
            if(client != null) {
                client.close();
            }
            diskShare = null;
            session = null;
            connection = null;
            client = null;

    }


    // Читаем
    private byte[] readFile(String filename) throws IOException {

        final File file = getFile(filename, AccessMask.GENERIC_READ, SMB2CreateDisposition.FILE_OPEN);
        InputStream is = file.getInputStream();
        byte[] fileBytes = null;
        BufferedInputStream in = new BufferedInputStream(is);
        fileBytes = in.readAllBytes();
        return fileBytes;

    }

    //Записываем
    private boolean writeBytesArray(String filePathToSave, byte[] bytes) throws IOException {

        /*Set<AccessMask> accessMask = new HashSet<AccessMask>(EnumSet.of(AccessMask.FILE_ADD_FILE));
        Set<SMB2CreateOptions> createOptions = new HashSet<SMB2CreateOptions>(
                EnumSet.of(SMB2CreateOptions.FILE_NON_DIRECTORY_FILE, SMB2CreateOptions.FILE_WRITE_THROUGH));

        final File file = diskShare.openFile(filePathToSave, accessMask, null, SMB2ShareAccess.ALL,
                SMB2CreateDisposition.FILE_OVERWRITE_IF, createOptions);*/
        final File file = getFile(filePathToSave, AccessMask.FILE_ADD_FILE, SMB2CreateDisposition.FILE_OVERWRITE_IF);
        OutputStream out = file.getOutputStream();


        FilterOutputStream fos = new FilterOutputStream(out) {

            boolean isOpen = true;
            @Override
            public void close() throws IOException {
                if(isOpen) {
                    super.close();
                    isOpen=false;
                }
                file.close();
            }
        };

        fos.write(bytes);
        fos.flush();
        fos.close();







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

    private String writeFileToNetworkFolder(MultipartFile file, String directory, String fileName) throws IOException {
        //String filePath = String.format("./%s/%s/%s.xlsx", username, networkFileName, projectName);

        String[] split = directory.split("/");
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].trim();
            if(".".equals(split[i])){
                continue;
            }
            if("".equals(split[i])){
                continue;
            }
            if(i+1 < split.length){
                split[i+1] = String.format("%s/%s",split[i],split[i+1]);
            }
            try{
                createFolder(split[i]);
            } catch (IOException ex){

            }
        }

        String filePath = String.format("%s/%s", split[split.length-1], fileName);
        if(!this.writeBytesArray(filePath, file.getBytes())){
            throw new IOException(String.format("File: %s was not saved", filePath));
        }
        return filePath;
    }

    public String writeSourceFile(MultipartFile file, String projectName, String username) throws IOException {
        String directory = String.format("./%s/%s", username, projectName);
        String filename = "source.xlsx";
        return writeFileToNetworkFolder(file, directory, filename);
    }


    private File getFile(String filename, AccessMask accessMask, SMB2CreateDisposition createDisposition) {
        Set<SMB2ShareAccess> shareAccess = new HashSet<SMB2ShareAccess>();
        shareAccess.addAll(SMB2ShareAccess.ALL);

        Set<SMB2CreateOptions> createOptions = new HashSet<SMB2CreateOptions>();
        createOptions.add(SMB2CreateOptions.FILE_WRITE_THROUGH);

        Set<AccessMask> accessMaskSet = new HashSet<AccessMask>();
        accessMaskSet.add(accessMask);
        File file;

        file = diskShare.openFile(filename, accessMaskSet, null, shareAccess, createDisposition, createOptions);
        return file;
    }

    private Directory getFolder(String filename, AccessMask accessMask, SMB2CreateDisposition createDisposition) {
        Set<SMB2ShareAccess> shareAccess = new HashSet<SMB2ShareAccess>();
        shareAccess.addAll(SMB2ShareAccess.ALL);

        Set<AccessMask> accessMaskSet = new HashSet<AccessMask>();
        accessMaskSet.add(accessMask);

        Directory file;
        file = diskShare.openDirectory(filename, accessMaskSet, null, shareAccess, createDisposition, null);
        return file;
    }

    private void createFolder(String folder) throws IOException {
        if (folderExists(folder)) {
            throw new IOException("Create directory for [" + folder + "] has failed. Directory already exists.");
        } else {
            diskShare.mkdir(folder);
        }
    }

    public boolean folderExists(String folder) throws IOException {
        return isFolder(folder);
    }

    public boolean isFolder(String f) throws IOException {
        try {
            return diskShare.getFileInformation(f).getStandardInformation().isDirectory();
        }catch(SMBApiException e) {
            if(NtStatus.valueOf(e.getStatusCode()).equals(NtStatus.STATUS_OBJECT_NAME_NOT_FOUND)) {
                return false;
            }
            if(NtStatus.valueOf(e.getStatusCode()).equals(NtStatus.STATUS_DELETE_PENDING)) {
                return false;
            }

            throw new IOException(e);
        }

    }

}
