package ru.AntonSibgatulin.bladeslayer.Players;
public class Square {
    public int x, y, w, h;

   public Square() {
    }

    public Square(int _x, int _y, int _w, int _h) {
        x = _x;
        y = _y;
        w = _w;
        h = _h;
    }

    //пересечение квадратов
    public static boolean isIntersect(Square a, Square b) {
        return ((a.x < (b.x + b.w)) &&
                (b.x < (a.x + a.w)) &&
                (a.y < (b.y + b.h)) &&
                (b.y < (a.y + a.h)));
    }

    //пересечение квадратов с выталкиванием
    public static boolean isLockIntersect(Square a, Square b) {
        if (!isIntersect(a, b))
            return false;
        int x0 = b.x - (a.x - b.w);
        int y0 = b.y - (a.y - b.h);
        int x1 = (a.x + a.w) - b.x;
        int y1 = (a.y + a.h) - b.y;
        if (x1 < x0)
            x0 = -x1;
        if (y1 < y0)
            y0 = -y1;

        if (Math.abs(x0) < Math.abs(y0))
            a.x += x0;
        else if (Math.abs(x0) > Math.abs(y0))
            a.y += y0;
        else {
            a.x += x0;
            a.y += y0;
        }
        return true;
    }
};