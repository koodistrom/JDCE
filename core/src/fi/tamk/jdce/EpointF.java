package fi.tamk.jdce;

//luokka copy pastettu https://www.stkent.com/2015/07/03/building-smooth-paths-using-bezier-curves.html
public class EpointF {

    private final float x;
    private final float y;

    public EpointF(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public EpointF plus(float factor, EpointF ePointF) {
        return new EpointF(x + factor * ePointF.x, y + factor * ePointF.y);
    }

    public EpointF plus(EpointF ePointF) {
        return plus(1.0f, ePointF);
    }

    public EpointF minus(float factor, EpointF ePointF) {
        return new EpointF(x - factor * ePointF.x, y - factor * ePointF.y);
    }

    public EpointF minus(EpointF ePointF) {
        return minus(1.0f, ePointF);
    }

    public EpointF scaleBy(float factor) {
        return new EpointF(factor * x, factor * y);
    }

}
