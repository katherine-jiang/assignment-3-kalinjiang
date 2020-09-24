package assignment1;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ContentInfoTest {
    @Test
    void testContentInfo()  {
        try {
            // url
            URL myURL = new URL("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/12lines.txt");
            new ContentInfo(myURL);
        }  catch (NullPointerException | MalformedURLException e) {
            fail("unexpected NullPointerException");
        }


        try {
            // null url
            URL myURL = null;
            new ContentInfo(myURL);
            fail("expected NullPointerException");
        }  catch (NullPointerException e) {
            // NullPointerException caught
        }

        try {
            // string
            new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/12lines.txt");
        } catch (MalformedURLException e) {
            fail("unexpected MalformedURLException");
        }

        try {
            // invalid url string
            new ContentInfo("wrong.url");
            fail("unexpected MalformedURLException");
        } catch (MalformedURLException e) {
            // MalformedURLException caught
        }
    }

    @Test
    void TestGetContentLength()  {
        try {
            ContentInfo myContent1 = new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/12lines.pdf");
            int myContent1Length = myContent1.getContentLength();
            assertEquals(12811, myContent1Length);

            ContentInfo myContent2 = new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/12lines.txt");
            int myContent2Length = myContent2.getContentLength();
            assertEquals(649, myContent2Length);

            ContentInfo myContent3 = new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/500x200.png");
            int myContent3Length = myContent3.getContentLength();
            assertEquals(68643, myContent3Length);
        } catch (MalformedURLException e) {
            fail("unexpected MalformedURLException");
        } catch (IOException e) {
            fail("unexpected IOException");
        }
    }

    @Test
    void TestGetContentType() {
        try {
            ContentInfo myContent1 = new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/12lines.pdf");
            String myContent1Type = myContent1.getContentType();
            assertEquals("application/pdf", myContent1Type);

            ContentInfo myContent2 = new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/12lines.txt");
            String myContent2Type = myContent2.getContentType();
            assertTrue(myContent2Type.startsWith("text/plain"));

            ContentInfo myContent3 = new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/500x200.png");
            String myContent3Type = myContent3.getContentType();
            assertEquals("image/png", myContent3Type);
        } catch (MalformedURLException e) {
            fail("unexpected MalformedURLException");
        } catch (IOException e) {
            fail("unexpected IOException");
        }
    }

    @Test
    void TestGetImageSize() {
        ContentInfo myContent = null;
        try {
            myContent = new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/500x200.png");
        } catch (MalformedURLException e) {
            fail("unexpected MalformedURLException");
        }
        Dimension myDimension = null;
        try {
            myDimension = myContent.getImageSize();
        } catch (IOException e) {
            fail("unexpected IOException");
        }
        int width = myDimension.width;
        int height = myDimension.height;
        assertEquals(500 , width);
        assertEquals(200 , height);
    }

    @Test
    void TestGetLastModified() {
        try {
            ContentInfo myContent1 = new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/12lines.pdf");
            Date myContent1LastModDate= myContent1.getLastModified();
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("YYYY");
            assertEquals("2020", simpleDateFormat1.format(myContent1LastModDate));

            ContentInfo myContent2 = new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/12lines.txt");
            Date myContent2LastModDate= myContent2.getLastModified();
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("YYYY");
            assertEquals("2020", simpleDateFormat2.format(myContent2LastModDate));

            ContentInfo myContent3 = new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/500x200.png");
            Date myContent3LastModDate= myContent3.getLastModified();
            SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("YYYY");
            assertEquals("2020", simpleDateFormat3.format(myContent3LastModDate));
        } catch (MalformedURLException e) {
            fail("unexpected MalformedURLException");
        } catch (IOException e) {
            fail("unexpected IOException");
        }
    }

    @Test
    void TestGetLineCount()  {
        ContentInfo myContent = null;
        try {
            myContent = new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/12lines.txt");
        } catch (MalformedURLException e) {
            fail("unexpected MalformedURLException");
        }
        try {
            int lines = myContent.getLineCount();
            assertEquals(12, lines);
        } catch (IOException e) {
            fail("unexpected IOException");
        }
    }

    @Test
    void TestGetLocation() {
        try {
            ContentInfo myContent = new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/12lines.pdf");
            String location = myContent.getLocation();
            assertEquals("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/12lines.pdf", location);
        } catch (MalformedURLException e) {
            fail("unexpected MalformedURLException");
        }
    }

    @Test
    void TestIsAvailable() {
        try {
            //is Available content
            ContentInfo myContent1 = new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/12lines.pdf");
            assertTrue(myContent1.isAvailable());

            //non available content
            ContentInfo myContent2 = new ContentInfo("https://no.available/500x200.png");
            assertFalse(myContent2.isAvailable());
        } catch (MalformedURLException e) {
            fail("unexpected MalformedURLException");
        }
    }

    @Test
    void TestIsImage()  {

        // image content
        ContentInfo myImageContent = null;
        try {
            myImageContent = new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/500x200.png");
        } catch (MalformedURLException e) {
            fail("unexpected MalformedURLException");
        }
        try {
            assertTrue(myImageContent.isImage());
        } catch (IOException e) {
            fail("unexpected IOException");
        }


        //non image content
        ContentInfo myTextContent = null;
        try {
            myTextContent = new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/12lines.txt");
        } catch (MalformedURLException e) {
            fail("unexpected MalformedURLException");
        }
        try {
            assertFalse(myTextContent.isImage());
        } catch (IOException e) {
            fail("unexpected IOException");
        }


    }

    @Test
    void TestIsText() {
        //text content
        ContentInfo myTextContent = null;
        try {
            myTextContent = new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/12lines.txt");
        } catch (MalformedURLException e) {
            fail("unexpected MalformedURLException");
        }
        try {
            assertTrue(myTextContent.isText());
        } catch (IOException e) {
            fail("unexpected IOException");
        }

        // image content
        ContentInfo myImageContent = null;
        try {
            myImageContent = new ContentInfo("https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/500x200.png");
        } catch (MalformedURLException e) {
            fail("unexpected MalformedURLException");
        }
        try {
            assertFalse(myImageContent.isText());
        } catch (IOException e) {
            fail("unexpected IOException");
        }
    }

}