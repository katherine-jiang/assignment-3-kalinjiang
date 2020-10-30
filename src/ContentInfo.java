
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * @author Kailin Jiang
 * @since 10/29/2020
 */
public class ContentInfo {
    URL url;

    /**
     * Create an instance with the content URL
     *
     * @param url a url
     * @throws NullPointerException if url is null
     */
    public ContentInfo(URL url){
        if(url == null){
            throw new NullPointerException();
        }
        this.url = url;
    }

    /**
     * Create an instance with a string representing the content URL.
     *
     * @param url a string
     * @throws MalformedURLException if string is invalid url string
     */
    public ContentInfo(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    /**
     * Gets the length of this content in bytes.
     *
     * @return the length of content
     * @throws IOException if content is unavailable
     */
    public int getContentLength() throws IOException {
        URLConnection urlConnection = getConnection();
        return urlConnection.getContentLength();
    }

    /**
     * Gets the content type for this content (type/subtype).
     *
     * @return the the content type
     * @throws IOException if content is unavailable
     */
    public String getContentType() throws IOException {
        URLConnection urlConnection = getConnection();
        return urlConnection.getContentType();
    }

    /**
     * Gets the dimension for image content.
     *
     * @return the dimension of image
     * @throws IOException if content is unavailable
     * @throws IllegalStateException if content is not an image
     */
    public Dimension getImageSize() throws IOException {
        if (isImage()){
            URLConnection urlConnection = getConnection();
            InputStream in = urlConnection.getInputStream();
            BufferedImage image = ImageIO.read(in);
            int width = image.getWidth();
            int height = image.getHeight();
            return new Dimension(width, height);
        }
        return null;
    }

    /**
     * Gets the date that this content was last modified.
     *
     * @return the date of last modifying
     * @throws IOException if content is unavailable
     */
    public Date getLastModified() throws IOException {
        URLConnection urlConnection = getConnection();
        long date = urlConnection.getLastModified();
        return new Date(date);
    }

    /**
     * Gets the line count for text content.
     *
     * @return the number of lines in text content
     * @throws IOException if content is unavailable
     * @throws IllegalStateException if content is not a text
     */
    public int getLineCount() throws IOException, IllegalStateException { //works
        if (!isText()) {
            throw new IllegalStateException();
        }

        URLConnection urlConnection = getConnection();
        int lines = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        while (reader.readLine() != null) lines++;
        reader.close();
        return lines;
    }

    /**
     * Gets the location for this content.
     *
     * @return the location for this content
     */
    public String getLocation() {
        return url.toString();
    }

    /**
     * Determines whether the content is available.
     *
     * @return true if content is available, otherwise false
     */
    public boolean isAvailable(){
        try {
            URLConnection urlConnection = getConnection();
            urlConnection.connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Determines whether the content is an image.
     *
     * @return true if content is image, otherwise false
     * @throws IOException if content is unavailable
     */
    public boolean isImage() throws IOException {
        URLConnection urlConnection = getConnection();
        String contentType = urlConnection.getHeaderField("Content-Type");
        return contentType.startsWith("image/");
    }

    /**
     * Determines whether the content is text.
     *
     * @return true if content is text, otherwise false
     * @throws IOException if content is unavailable
     */
    public boolean isText() throws IOException {
        URLConnection urlConnection = getConnection();
        String contentType = urlConnection.getHeaderField("Content-Type");
        return contentType.startsWith("text/");
    }

    //new feature

    /**
     * Determines whether the content is audio.
     *
     * @return true if content is audio, otherwise false
     * @throws IOException if content is unavailable
     */
    public boolean isAudio() throws IOException{
        URLConnection urlConnection = getConnection();
        String contentType = urlConnection.getHeaderField("Content-Type");
        return contentType.startsWith("audio/");
    }


    public double getAudioDuration() throws IOException, UnsupportedAudioFileException {
        AudioInputStream stream = null;
        stream = AudioSystem.getAudioInputStream(url);
        AudioFormat format = stream.getFormat();
        if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format
                    .getSampleRate(), format.getSampleSizeInBits() * 2, format
                    .getChannels(), format.getFrameSize() * 2, format
                    .getFrameRate(), true); // big endian
            stream = AudioSystem.getAudioInputStream(format, stream);
        }

        DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat(),
                ((int) stream.getFrameLength() * format.getFrameSize()));
        Clip clip = null;
        try {
            clip = (Clip) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        clip.close();
        double duration = clip.getBufferSize()
                / (clip.getFormat().getFrameSize() * clip.getFormat()
                .getFrameRate());

        return Math.round(duration*1000)/1000.0d;
    }


    private URLConnection getConnection() throws IOException {
        URLConnection urlConnection = url.openConnection();
        if (urlConnection instanceof HttpURLConnection){
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("GET");
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                return httpURLConnection;
            }else {
                throw new IOException();
            }
        }else {
            return urlConnection;
        }
    }

}
