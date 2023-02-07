package zybandroid.util.touchevents;

import android.graphics.RectF;
import android.view.MotionEvent;
import zybandroid.util.geometry.Pont;

/**
 *
 * @author zybon Created 2017.08.06. 12:23:28
 */
public class ZybTouchMuvelet {

    private static final String TAG = "ZybTouchMuvelet";
    /**
     * egy katt dupla katt hosszú katt mozgatás suhintás zoomolás
     */
//    private MotionEvent downMotionEvent = MotionEvent.obtain(0, 0, 0, 0, 0, 0); 

    private float downX = Integer.MIN_VALUE;
    private float downY = Integer.MIN_VALUE;
    private MotionEvent aktMotionEvent;
    private final MultiTouch multiTouch = new MultiTouch();

    private final Kattintasok kattintasok = new Kattintasok();

    //zoom
    private final ZybMotionRect motionRect0 = new ZybMotionRect();
    private final ZybMotionRect motionRect = new ZybMotionRect();
    private float zoomArany;

    public static final int ALLAPOT_SEMMI = 0;
    public static final int ALLAPOT_MOZGAS_START = 1;
    public static final int ALLAPOT_MOZGAS_FOLYAMATBAN = 1 << 1;
    public static final int ALLAPOT_ZOOM_START = 1 << 2;
    public static final int ALLAPOT_ZOOM_FOLYAMATBAN = 1 << 3;

//    public static final int ALLAPOT_DUPLAKATT = 1<<1;
//    public static final int ALLAPOT_SUHINTAS_X = 1<<2;
//    public static final int ALLAPOT_SUHINTAS_Y = 1<<3;    
    private int allapotFlag = ALLAPOT_SEMMI;

    private final Suhintas suhintas = new Suhintas();

    //mozgatas
    private final Mozgatas mozgatas = new Mozgatas();

    //suhintas
    private float MIN_MOVE = 5;

    private int action;
    private int actionMasked;
//    private String actionString = "";

    public ZybTouchMuvelet() {
    }

    public void setDensity(float density) {
        MIN_MOVE = 5 * density;
    }

    private void setAllapotFlag(int flag) {
//        allapotFlag = ALLAPOT_SEMMI;
        allapotFlag = flag;
    }

//    public boolean isZoom(){
//        return (allapotFlag & ZOOM_MASK) != 0;
//    }
    public int getAction() {
        return action;
    }

    public int getActionMasked() {
        return actionMasked;
    }

    public void touchEvent(MotionEvent event) {
        this.action = event.getAction();
        this.actionMasked = event.getActionMasked();
//        actionString = _getActionString(event.getActionMasked());
        setAktMotionEvent(MotionEvent.obtain(event));
        multiTouch.update(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchDown(event);
            return;
        }
        if (event.getPointerCount() == 2) {
//            Log.i(TAG, getActionString(event.getActionMasked()));
            if (event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {

//                Log.i(TAG, "zoomStart");
                zoomStart(event);
                return;
            }
            if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
                int lennMaradIndex = event.getActionIndex() == 0 ? 1 : 0;
                mozgatasStart(event.getX(lennMaradIndex), event.getY(lennMaradIndex));
                return;
            }
//            Log.i(TAG, "touchEvent: "+getActionMaskedString(event.getActionMasked()));
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            touchMove(event);
            return;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {

//            Log.i(TAG, "UP");
            touchUp(event);
//            return;
        }

    }

    public final String getActionString(int actionMasked) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return "ACTION_DOWN";
            case MotionEvent.ACTION_MOVE:
                return "ACTION_MOVE";
            case MotionEvent.ACTION_UP:
                return "ACTION_UP";
            case MotionEvent.ACTION_POINTER_DOWN:
                return "ACTION_POINTER_DOWN";
            case MotionEvent.ACTION_POINTER_UP:
                return "ACTION_POINTER_UP";
        }
        return Integer.toHexString(action);
    }

//    public String getActionString() {
//        return actionString;
//    }
    private void touchDown(MotionEvent event) {
        mozgatas.reset();
//        downMotionEvent = MotionEvent.obtain(event);
        downX = event.getX();
        downY = event.getY();
        mozgatasStart(event.getX(), event.getY());

    }

    private void touchMove(MotionEvent event) {

        if (event.getPointerCount() == 1) {
            if (allapotFlag == ALLAPOT_MOZGAS_START) {
                setAllapotFlag(ALLAPOT_MOZGAS_FOLYAMATBAN);
            }
            if (allapotFlag == ALLAPOT_MOZGAS_FOLYAMATBAN) {
                mozgatas.addMove(
                        event.getX(event.getActionIndex()),
                        event.getY(event.getActionIndex()),
                        System.currentTimeMillis());
            }
            return;
        }
        if (event.getPointerCount() == 2) {
            zoomTouch(event);
        }
    }

    private void mozgatasStart(float mozgKezdX, float mozgKezdY) {
        setAllapotFlag(ALLAPOT_MOZGAS_START);
        mozgatas.setStartPos(mozgKezdX, mozgKezdY);

    }

    private void mozgatasTouch(MotionEvent event) {
//        mozgatasX.ertek = event.getX(event.getActionIndex())-mozgatasX.startPos;

//        if (!mozgatasX.megkezdve) {
//            mozgatasX.megkezdve = Math.abs(mozgatasX.ertek)>MIN_MOVE;
//            if (mozgatasX.megkezdve && !mozgatasY.megkezdve) {
//                onMozgatasXKezd();
//            }
//        }
    }

    private void zoomStart(MotionEvent event) {
//        if (!isZoom()) {
        motionRect0.set(event.getX(0), event.getY(0),
                event.getX(1), event.getY(1)
        );
        setAllapotFlag(ALLAPOT_ZOOM_START);
//            return;
//        }    
    }

    private void zoomTouch(MotionEvent event) {
        if (allapotFlag == ALLAPOT_ZOOM_START) {
            setAllapotFlag(ALLAPOT_ZOOM_FOLYAMATBAN);
        }
        motionRect.set(event.getX(0), event.getY(0),
                event.getX(1), event.getY(1)
        );
        zoomArany = motionRect.atlo / motionRect0.atlo;
    }

    private void touchUp(MotionEvent event) {
        kattintasok.set(event);
        mozgatas.sebessegSzamitas();
//        Log.i(TAG, "moveDb: "+moveDb);
//        Log.i(TAG, mozgatas.toString());
//        Log.i(TAG, suhintas.toString());
    }

//    public MotionEvent getDownMotionEvent() {
//        return downMotionEvent;
//    }    
    public float getDownX() {
        return downX;
    }

    public float getDownY() {
        return downY;
    }

    private void setAktMotionEvent(MotionEvent aktMotionEvent) {
        this.aktMotionEvent = aktMotionEvent;
    }

    public MotionEvent getAktMotionEvent() {
        return aktMotionEvent;
    }

    public void offsetAktMotion(float deltaX, float deltaY) {
        aktMotionEvent.offsetLocation(deltaX, deltaY);
    }

    public float getAktX() {
        return aktMotionEvent.getX();
    }

    public float getAktY() {
        return aktMotionEvent.getY();
    }

    public final boolean isDuplaKatt() {
        return kattintasok.isDuplaKatt();
    }

    public final int getAllapot() {
        return allapotFlag;
    }

    public final float getZoomArany() {
        return zoomArany;
    }

    public float getMotionRect0KozepX() {
        return motionRect0.kozepX;
    }

    public float getMotionRect0KozepY() {
        return motionRect0.kozepY;
    }

    public ZybMotionRect getMotionRect0() {
        return motionRect0;
    }

    public float getMotionRectKozepX() {
        return motionRect.kozepX;
    }

    public float getMotionRectKozepY() {
        return motionRect.kozepY;
    }

    public ZybMotionRect getMotionRect() {
        return motionRect;
    }

    public final float getElmozdulasX() {
        return mozgatas.elmozdulasX;
    }

    public final float getElmozdulasY() {
        return mozgatas.elmozdulasY;
    }

    public final float getLastElmozdulasX() {
        return mozgatas.lastElmozdulasX;
    }

    public final float getLastElmozdulasY() {
        return mozgatas.lastElmozdulasY;
    }

    public final boolean isElmozdulasXiranyba() {
        return mozgatas.isElmozdulasXiranyba();
    }

    public final boolean isElmozdulasYiranyba() {
        return !mozgatas.isElmozdulasXiranyba();
    }

    public final long getUpDownTime() {
        return kattintasok.masodik.tartam;
    }

    /**
     * A suhintás vízszintes irányú sebessége.
     * <p>
     * 0,1s alatt bekövetkezett vízszintes irányú elmozdulás
     * </p>
     *
     * @return sebesség ( 1px / 0,1s)
     */
    public final float getSuhintasSebessegX() {
        return suhintas.sebessegX;
    }

    /**
     * A suhintás vízszintes iránya.
     * <ul>
     * <li>-1: balra</li>
     * <li>0: áll</li>
     * <li>1: jobbra</li>
     * </ul>
     *
     * @return irány
     */
    public final float getSuhintasIranyX() {
        return suhintas.iranyX;
    }

    /**
     * A suhintás függőleges irányú sebessége.
     * <p>
     * 0,1s alatt bekövetkezett függőleges irányú elmozdulás
     * </p>
     *
     * @return sebesség ( 1px / 0,1s)
     */
    public final float getSuhintasSebessegY() {
        return suhintas.sebessegY;
    }

    /**
     * A suhintás függőleges iránya.
     * <ul>
     * <li>-1: felfelé</li>
     * <li>0: áll</li>
     * <li>1: lefelé</li>
     * </ul>
     *
     * @return irány
     */
    public final float getSuhintasIranyY() {
        return suhintas.iranyY;
    }

    public Move[] getMoves() {
        return mozgatas.moves;
    }

    public int getAktMoveDb() {
        return mozgatas.aktMoveDb;
    }

    private class Kattintas {

        long downTime = 0;
        long upTime = 0;

        long tartam;

        float upDownTav;

        void set(Kattintas masikKattintas) {
            this.downTime = masikKattintas.downTime;
            this.upTime = masikKattintas.upTime;
            this.tartam = masikKattintas.tartam;
            this.upDownTav = masikKattintas.upDownTav;
        }

        void set(MotionEvent event) {
            this.downTime = event.getDownTime();
            this.upTime = event.getEventTime();
            this.tartam = upTime - downTime;
//            if (event == null) {
//                throw new AssertionError("event == null");
//            }
//            if (downMotionEvent == null) {
//                throw new AssertionError("downMotionEvent == null");
//            }
//            this.upDownTav = getTav(event.getX()-downMotionEvent.getX(), 
//                    event.getY()-downMotionEvent.getY());
            this.upDownTav = getTav(event.getX() - downX,
                    event.getY() - downY);
        }

        private float getTav(float x, float y) {
            return (float)Math.sqrt(x * x + y * y);
        }

        @Override
        public String toString() {
            return "EventIdo{" + "downTime=" + downTime + ", upTime=" + upTime + '}';
        }

    }

    private class Kattintasok {

        final Kattintas elso = new Kattintas();
        final Kattintas masodik = new Kattintas();

        long koztesIdo;

        void set(MotionEvent event) {
            elso.set(masodik);
            masodik.set(event);
            koztesIdo = masodik.downTime - elso.upTime;
        }

        boolean isDuplaKatt() {
            return ((koztesIdo) < 200)
                    && ((elso.tartam) < 100)
                    && ((masodik.tartam) < 100)
                    && ((elso.upDownTav) < MIN_MOVE)
                    && ((masodik.upDownTav) < MIN_MOVE);
        }

        @Override
        public String toString() {
            return "EventIdok{" + "elso=" + elso + ", masodik=" + masodik + '}';
        }
    }

    private class Suhintas {

        private float iranyX;
        private float sebessegX;
        private float iranyY;
        private float sebessegY;

        private void nullaz() {
            iranyX = 0;
            sebessegX = 0;
            iranyY = 0;
            sebessegY = 0;
        }

        private void setX(float _sebessegX) {
            sebessegX = Math.abs(_sebessegX);
            iranyX = sebessegX == 0 ? 0 : (sebessegX / _sebessegX);
        }

        private void setY(float _sebessegY) {
            sebessegY = Math.abs(_sebessegY);
            iranyY = sebessegY == 0 ? 0 : (sebessegY / _sebessegY);
        }

        @Override
        public String toString() {
            return "Suhintas{" + "iranyX=" + iranyX + ", sebessegX=" + sebessegX + ", iranyY=" + iranyY + ", sebessegY=" + sebessegY + '}';
        }

    }

    public class Mozgatas {

        public static final int MOVE_DB = 5;
        Move[] moves = new Move[MOVE_DB];
        int aktMoveDb;

        float startPosX;
        float startPosY;

        float elmozdulasX;
        float elmozdulasY;
        private float lastElmozdulasX;
        private float lastElmozdulasY;

        public Mozgatas() {
            for (int i = 0; i < MOVE_DB; i++) {
                moves[i] = new Move();
            }
        }

        void reset() {
            for (Move move : moves) {
                move.reset();
            }
            aktMoveDb = 0;
        }

        private void setStartPos(float startPosX, float startPosY) {
            this.startPosX = startPosX;
            this.startPosY = startPosY;
            addMove(startPosX, startPosY, System.currentTimeMillis());
        }

        private void addMove(float x, float y, long currentTimeMillis) {
            //előző Move-ok arrébtolása
            for (int i = MOVE_DB - 1; i > 0; i--) {
                moves[i].setFrom(moves[i - 1]);
            }

            if (aktMoveDb < MOVE_DB) {
                aktMoveDb++;
            }
            moves[0].set(x, y, currentTimeMillis);
//            if (aktMoveDb>1){
//                moves[0].setSebesseg(moves[1]);
//            }
            elmozdulasX = x - startPosX;
            elmozdulasY = y - startPosY;
            if (aktMoveDb > 1) {
                lastElmozdulasX = x - moves[1].x;
                lastElmozdulasY = y - moves[1].y;
            }
        }

        void sebessegSzamitas() {
            if (aktMoveDb == 0) {
                return;
            }
            if (aktMoveDb == 1) {
                suhintas.nullaz();
                return;
            }
//            for (int i = 1; i <= aktMoveDb; i++) {
//                moves[i-1].setSebesseg(moves[i]);
//                
//            }
//            Log.i(TAG, "sebessegSzamitas\n\t"+moves[0]+ "\n\t" +moves[aktMoveDb-1]);
            suhintas.setX(moves[0].getSebessegX(moves[aktMoveDb - 1]));
            suhintas.setY(moves[0].getSebessegY(moves[aktMoveDb - 1]));
        }

        boolean isElmozdulasXiranyba() {
            return Math.abs(elmozdulasX) > Math.abs(elmozdulasY);
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            str.append("aktMoveDb: ");
            str.append(aktMoveDb);
            str.append('\n');
            for (int i = 0; i < MOVE_DB; i++) {
                str.append(i);
                str.append(".: ");
                str.append(moves[i]);
                str.append('\n');
            }
            return str.toString(); //To change body of generated methods, choose Tools | Templates.
        }

    }

    public class Move {

        public float x;
        public float y;
        public long ido;
//        public float sebessegX;
//        public float sebessegY;

        private void set(float x, float y, long ido) {
            this.x = x;
            this.y = y;
            this.ido = ido;
//            this.sebessegX = 0;
//            this.sebessegY = 0;
        }

        private void setFrom(Move masik) {
            this.x = masik.x;
            this.y = masik.y;
            this.ido = masik.ido;
//            this.sebessegX = masik.sebessegX;
//            this.sebessegY = masik.sebessegY;            
        }

//        private void setSebesseg(Move masik){
//            this.sebessegX = getSebessegX(masik);
//            this.sebessegY = getSebessegY(masik);
//        }
        float getSebessegX(Move masik) {
            return (x - masik.x) * 100 / ((ido + 1) - masik.ido);
        }

        float getSebessegY(Move masik) {
            return (y - masik.y) * 100 / ((ido + 1) - masik.ido);
        }

        private void reset() {
            x = 0;
            y = 0;
            ido = 0;
//            sebessegX = 0;
//            sebessegY = 0;            
        }

        @Override
        public String toString() {
            return "Move{" + "x=" + x + ", y=" + y + ", ido=" + ido
                    + //                    ", sebessegX=" + sebessegX + ", sebessegY=" + sebessegY +
                    '}';
        }

    }

    public static final class VirtualRect {

        private final RectF rectF = new RectF();

        private void set(float _left, float _top, float _right, float _bottom) {
            rectF.set(_left, _top, _right, _bottom);
        }

        private void set(VirtualRect virtKepRect) {
            rectF.set(virtKepRect.rectF);
        }

        public final float left() {
            return rectF.left;
        }

        public final float top() {
            return rectF.top;
        }

        public final float right() {
            return rectF.right;
        }

        public final float bottom() {
            return rectF.bottom;
        }

        public final float height() {
            return rectF.height();
        }

        public final float width() {
            return rectF.width();
        }

    }

    public int getAktTouchId() {
        return multiTouch.aktId;
    }

    public boolean existTouchPont(int id) {
        return multiTouch.hasPont(id);
    }

    public TouchPont getAktTouchPont() {
        if (multiTouch.hasPont(multiTouch.aktId)) {
            return multiTouch.touchPoints[multiTouch.aktId];
        }
        return null;
    }

    public TouchPont getTouchPont(int id) {
        if (multiTouch.hasPont(id)) {
            return multiTouch.touchPoints[id];
        }
        return null;
    }

    public TouchPont[] getTouchPonts() {
        return multiTouch.touchPoints;
    }

    public static class TouchPont {

        public final int id;
        public final Pont pont;
        public int state;

        public static final String[] states = new String[]{
            "SEMMI",
            "DOWN",
            "MOVE"
        };

        private TouchPont(int id) {
            pont = new Pont();
            this.id = id;
            state = 0;
        }

        @Override
        public String toString() {
            return "TP: [ID: " + id + ", state: " + states[state] + ", x=" + pont.x + ", y=" + pont.y + ']';
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof TouchPont)) {
                return false;
            }
            TouchPont masik = (TouchPont) o;
            return masik.id == id;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 17 * hash + this.id;
            return hash;
        }

    }

    private class MultiTouch {

        private final int[] ids = new int[10];
        private final TouchPont[] touchPoints = new TouchPont[10];
        private int aktId;

        public MultiTouch() {
            for (int i = 0; i < touchPoints.length; i++) {
                touchPoints[i] = new TouchPont(i);
            }
        }

        void downPont(int id, float x, float y) {
            ids[id] = 1;
            touchPoints[id].pont.set(x, y);
            touchPoints[id].state = 1;
        }

        void movePont(int id, float x, float y) {
            if (ids[id] == 0) {
                return;
            }
            touchPoints[id].pont.set(x, y);
            touchPoints[id].state = 2;
        }

        void upPont(int id, float x, float y) {
            if (ids[id] == 0) {
                return;
            }
            touchPoints[id].pont.set(x, y);
            touchPoints[id].state = 0;

        }

        void removePont(int id) {
            ids[id] = 0;
            touchPoints[id].state = 0;
        }

        boolean hasPont(int id) {
            return ids[id] != 0;
        }

//        Pont getPont(int id){
//            if (ids[id] == 0) {return null;}
//            return touchPoints[id];
//        }
        private void reset() {
            for (int i = 0; i < ids.length; i++) {
                ids[i] = 0;
                touchPoints[i].state = 0;
            }
        }

        private void update(MotionEvent event) {
            aktId = event.getPointerId(event.getActionIndex());
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                reset();
                downPont(aktId, event.getX(), event.getY());
                return;
            }
            if (event.getPointerCount() > 1) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                        int find = event.findPointerIndex(aktId);
                        downPont(aktId, event.getX(find), event.getY(find));
                        return;
                    case MotionEvent.ACTION_POINTER_UP:
                        removePont(aktId);
                        return;
                }

            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                for (int i = 0; i < ids.length; i++) {
                    if (ids[i] != 0) {
                        int find = event.findPointerIndex(i);
                        if (find == -1) {
                            removePont(i);
                            continue;
                        }
                        movePont(i, event.getX(find), event.getY(find));
                    }

                }

                return;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                reset();
//                return;
            }
        }

//        private TouchPont[] getAktivTouchPonts() {
//            int db = 0;
//            for (int i = 0; i < ids.length; i++) {
//                if (ids[i] != 0) {db++;}
//            }
////            TouchPont[] tp = new TouchPont[db];
////            for (int i = 0; i < touchPoints.length; i++) {
////                tp[i] = touchPoints[]
////            }
//        }
    }

}
