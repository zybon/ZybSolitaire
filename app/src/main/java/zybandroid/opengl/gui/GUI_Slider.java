package zybandroid.opengl.gui;

import zybandroid.opengl.blenderdatas.MeshData;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.text.TextObject;
import zybandroid.opengl.util.ZybColor;
import zybandroid.util.touchevents.ZybMultiTouchEvent;

/**
 *
 * @author zybon
 * Created 2021.04.20. 12:17:10
 */
public class GUI_Slider implements GUI_PanelElement{

    private final GUI_Button contanier;
    private final GUI_Button value;

    public GUI_Slider(MeshData contanierMeshData, MeshData valueMeshData) {
        contanier = new GUI_Button(contanierMeshData);
        value = new GUI_Button(valueMeshData);

    }

    public void setValueColor(ZybColor color){
        value.setBaseColor(color);
    }

    public void setPercentValue(float percent){
        value.setPercentValue(percent);

    }

    public void setPositionInOpenGl(Vector3D position){
        contanier.setPositionInOpengGl(position);
        value.setPositionInOpengGl(position);
    }

    @Override
    public void onTouchEvent(ZybMultiTouchEvent zmte) {

    }

    @Override
    public void draw() {
        contanier.draw();
        value.draw();
    }


}
