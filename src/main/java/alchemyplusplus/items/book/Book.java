package alchemyplusplus.items.book;

import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.ArrayList;

public class Book
{

    public static Book alchemicalGude;

    public static Book load(String bookName)
    {
        Book book = new Book();

        File dataFile = new File(Minecraft.getMinecraft().mcDataDir.toString() + "/mods/bookData/" + bookName + ".book");
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(dataFile));

            int pageAmount = Integer.parseInt(br.readLine());
            book.pageAmount = pageAmount;
            book.pages = new ArrayList<BookPage>(pageAmount);

            for (int i = 0; i < pageAmount; i++)
            {
                BookPage curr = new BookPage();
                if (!curr.load(Minecraft.getMinecraft().mcDataDir.toString() + "/mods/bookData/" + br.readLine() + ".page"))
                {
                    br.close();
                    return book;
                }
                book.pages.add(curr);
            }

            br.close();
            book.loaded = true;
            return book;

        } catch (FileNotFoundException e)
        {
            System.err.println("Unable to load " + bookName + "! Description file is missing!");
        } catch (IOException e)
        {
            System.err.println("Unable to load " + bookName + "! Data corrupted!");
        }

        return book;
    }

    public static void loadAll()
    {
        alchemicalGude = load("alchemicalGuide");
    }
    private boolean loaded = false;

    public int pageAmount = 0;

    public ArrayList<BookPage> pages;

    public BookPage getPage(int number)
    {
        return this.pages.get(number);
    }

    public boolean isLoaded()
    {
        return this.loaded;
    }


}
