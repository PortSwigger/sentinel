/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import attacks.AttackMain;
import attacks.AttackMain.AttackTypes;
import burp.IParameter;
import java.util.HashMap;
import util.BurpCallbacks;

/**
 *
 * @author unreal
 */
public class SentinelHttpParam implements IParameter {

    static final byte PARAM_PATH = 7;
    private byte type;
    private String name;
    private int nameStart;
    private int nameEnd;
    private String value;
    private int valueStart;
    private int valueEnd;
    private boolean performAttack = false;

    public SentinelHttpParam(IParameter burpParameter) {
        this.type = burpParameter.getType();

        this.name = burpParameter.getName();
        this.nameStart = burpParameter.getNameStart();
        this.nameEnd = burpParameter.getNameEnd();

        this.value = burpParameter.getValue();
        this.valueStart = burpParameter.getValueStart();
        this.valueEnd = burpParameter.getValueEnd();
    }

    public SentinelHttpParam(byte type,
            String name, int nameStart, int nameEnd,
            String value, int valueStart, int valueEnd)
    {
        this.type = type;
        this.name = name;
        this.nameStart = nameStart;
        this.nameEnd = nameEnd;
        this.value = value;
        this.valueStart = valueStart;
        this.valueEnd = valueEnd;
    }

    private HashMap<AttackMain.AttackTypes, AttackTypeData> attackTypes = new HashMap<AttackMain.AttackTypes, AttackTypeData>();

    public void setAttackType(AttackTypes attackType, boolean enabled, String selected) {
        AttackTypeData a = new AttackTypeData(enabled, selected);
        attackTypes.put(attackType, a);
    }

    public void setAttackType(AttackMain.AttackTypes attackType, boolean enabled) {
        AttackTypeData a = new AttackTypeData(enabled);
        attackTypes.put(attackType, a);
    }

    public AttackTypeData getAttackType(AttackMain.AttackTypes attackType) {
        if (attackTypes.containsKey(attackType)) {
            return attackTypes.get(attackType);
        } else {
            return new AttackTypeData(false);
        }
    }
    
    public void resetAttackTypes() {
        //attackTypes.clear();
        attackTypes = new HashMap<AttackMain.AttackTypes, AttackTypeData>();
    }
    

    public String getTypeStr() {
        switch (type) {
            case 0:
                return "GET";
            case 1:
                return "POST";
            case 2:
                return "COOKIE";
            case 3:
                return "XML";
            case 4:
                return "XML_ATTR";
            case 5:
                return "MULTIPART";
            case 6:
                return "JSON";
            case 7:
                return "PATH";
            default:
                return "unknown";
        }
    }

    public void changeValue(String value) {
        this.value = value;

        this.valueEnd = this.valueStart + value.length();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    public int getValueLen() {
        return valueEnd - valueStart;
    }

    @Override
    public byte getType() {
        return type;
    }

    public int getNameLen() {
        return nameEnd - nameStart;
    }

    @Override
    public int getNameStart() {
        return nameStart;
    }

    @Override
    public int getNameEnd() {
        return nameEnd;
    }

    @Override
    public int getValueStart() {
        return valueStart;
    }

    @Override
    public int getValueEnd() {
        return valueEnd;
    }

    boolean isThisParameter(IParameter newParam) {
        if (getName().equals(newParam.getName()) && getType() == newParam.getType()) {
            return true;
        } else {
            return false;
        }
    }

    void updateLocationWith(IParameter newParam) {
        this.valueStart = newParam.getValueStart();
        this.valueEnd = newParam.getValueEnd();
        this.nameStart = newParam.getNameStart();
        this.nameEnd = newParam.getNameEnd();
    }
}