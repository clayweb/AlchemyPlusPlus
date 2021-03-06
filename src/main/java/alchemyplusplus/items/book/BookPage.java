package alchemyplusplus.items.book;

import alchemyplusplus.gui.AlchemicalGuide;
import alchemyplusplus.reference.Textures;

import java.io.*;
import java.util.ArrayList;

public class BookPage
{
    //Image data for this page
    private ArrayList<BookPageImage> images;
    private String[] text = new String[32];
    private String[] title = new String[2];

    public void drawPage(AlchemicalGuide gui)
    {
        int x0 = (gui.width / 2) - 146, x1 = gui.width / 2, y0 = (gui.height - 180) / 2;

        //Rendering images on the page
        //First binding the texture
        gui.getRenderEngine().bindTexture(Textures.Page.ALL);
        //then render all the images from the list
        for (BookPageImage data : images)
        {
            gui.drawTexturedModalRect(data.page == 0 ? x0 + data.x : x1 + data.x, y0 + data.y, data.offX, data.offY, data.sizeX, data.sizeY);
        }

        for (int i = 0; i < 16; i++)
        {
            gui.getFontRenderer().drawString(text[i], x0 + 19, y0 + 14 + (i * 9), 0);
            gui.getFontRenderer().drawString(text[i + 16], x1 + 19, y0 + 14 + (i * 9), 0);
        }

        gui.getFontRenderer().drawString(title[0], x0 + 130 - (5 * title[0].length()), y0 + 162, 0);
        gui.getFontRenderer().drawString(title[1], x1 + 12, y0 + 162, 0);
    }

    public boolean load(String path)
    {
        System.out.println("Loading book page data: " + path);
        try
        {

            BufferedReader br = new BufferedReader(new FileReader(new File(path)));

            //reading titles
            for (int i = 0; i < 2; i++)
            {
                this.title[i] = br.readLine();
            }

            //reading text
            for (int i = 0; i < 32; i++)
            {
                this.text[i] = br.readLine();
            }

            int imageAmount = Integer.parseInt(br.readLine());
            images = new ArrayList<BookPageImage>(imageAmount);

            for (int i = 0; i < imageAmount; i++)
            {
                BookPageImage data = new BookPageImage();
                data.page = Integer.parseInt(br.readLine());
                data.x = Integer.parseInt(br.readLine());
                data.y = Integer.parseInt(br.readLine());
                data.offX = Integer.parseInt(br.readLine());
                data.offY = Integer.parseInt(br.readLine());
                data.sizeX = Integer.parseInt(br.readLine());
                data.sizeY = Integer.parseInt(br.readLine());

                images.add(data);
            }

            br.close();
            return true;
        } catch (FileNotFoundException e)
        {
            System.out.println("Unable to read page data! File is missing! " + path);
        } catch (IOException e)
        {
            System.out.println("Unable to read page data! Data is corrupted! " + path);
        }
        return false;
    }

}
