package app.micromarket.marza.homeserviceoficial.Utils;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {

    private File file;
    private UploadCallBacks listener;
    private static final int DEFAULT_BUFFER_SIZE = 4096;


    public ProgressRequestBody(File file, UploadCallBacks listener) {
        this.file = file;
        this.listener = listener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return MediaType.parse("application/pdf"); //only image
    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = file.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(file);
        long uploaded = 0;
        try {
            int read;
            android.os.Handler handler = new Handler(Looper.getMainLooper());
            while((read = in.read(buffer)) != -1)
            {
                handler.post(new ProgressUpdater(uploaded, fileLength));
                uploaded += read;
                sink.write(buffer,0,read);
            }
        }finally {
            {
                in.close();
            }

        }

    }

    private class ProgressUpdater implements Runnable {
        private long uploaded;
        private long fileLength;
        public ProgressUpdater(long uploaded, long fileLength) {
            this.fileLength = fileLength;
            this.uploaded = uploaded;
        }

        @Override
        public void run() {
            listener.onProgressUpdate((int)(100*uploaded/fileLength));
        }
    }
}
