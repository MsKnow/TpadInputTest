package com.know.tpadinputtest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Shaowei on 5/1/2016.
 */
public class BitmapStripe
{
    //
    Context context;

    //
    Bitmap      bitmap          = null;
    Canvas      bitmapCanvas   = null;

    Paint paint = new Paint();

    //
    BitmapStripe(Context activity)
    {
        this.context = activity;
    }


    void generateTexture(int width, int height, int w1, int w2)
    {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        bitmapCanvas = new Canvas(bitmap);

        paint.setColor(Color.BLACK);
        bitmapCanvas.drawRect(0, 0, width, height, paint);

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(w1);

        //
        float offset = width % (w1 + w2);

        if( (int)offset == 0 )
        {
            offset = w2 / 2;
        }
        else if (offset < w1)
        {
            offset += w1;
            offset /= 2.f;
            offset = w1 - offset;
            offset =-offset;
        }
        else
        {
            offset -= w1;
            offset /= 2.f;
        }

        for(int i = (int)offset + w1/2; i < width; i += w1 + w2)
        {
            bitmapCanvas.drawLine(0, i, width, i, paint);
        }

    }

    void saveBitmap(String fileName)
    {
        //
        File file = new File(fileName);

        if(file.exists()){
            file.delete();
        }

        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 90, out))
            {
                out.flush();
                out.close();


                //refresh the data, that shown to user
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile( new File(fileName) );
                intent.setData(uri);
                context.sendBroadcast(intent);

            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
