package attacks;

import gui.session.SessionManager;
import model.SentinelHttpMessage;
import model.SentinelHttpMessageAtk;
import model.SentinelHttpMessageOrig;
import model.SentinelHttpParam;
import util.BurpCallbacks;

/**
 * Interface for all attack classes
 * 
 * initialMessage: the message we want to attack
 * origParam:      the param we want to attack
 * attackData:     additional data from the user for this attack
 * 
 * @author Dobin
 */
public abstract class AttackI {
    protected SentinelHttpMessageOrig initialMessage;
    protected SentinelHttpParam origParam;
    protected String attackData;
    protected String mainSessionName;
    protected boolean followRedirect = false;
    
    public AttackI(SentinelHttpMessageOrig origHttpMessage, String mainSessionName, boolean followRedirect, SentinelHttpParam origParam) {
        this.initialMessage = origHttpMessage;
        this.mainSessionName = mainSessionName;
        this.followRedirect = followRedirect;
        this.origParam = origParam;
    }
    
    public AttackI(SentinelHttpMessageOrig origHttpMessage, String mainSessionName, boolean followRedirect, SentinelHttpParam origParam, String data) {
        this.initialMessage = origHttpMessage;
        this.mainSessionName = mainSessionName;
        this.followRedirect = followRedirect;
        this.origParam = origParam;
        this.attackData = data;
    }
    
    
    /* Will execute the next (or initial) attack
     * Returns true if more attacks are necessary/available
     */
    abstract public boolean performNextAttack();
    
    /* Get the last http message sent by performNextAttack()
     */
    abstract public SentinelHttpMessageAtk getLastAttackMessage();

    /*
     * 
     */
    protected SentinelHttpMessageAtk initAttackHttpMessage(String attack) {
        // Copy httpmessage
        SentinelHttpMessageAtk newHttpMessage = new SentinelHttpMessageAtk(initialMessage);

        // Set orig param
        newHttpMessage.getReq().setOrigParam(origParam);
        
        // Set change param
        SentinelHttpParam changeParam = new SentinelHttpParam(origParam);
        if (attack != null) {
            changeParam.changeValue(attack);
        } else {
            //BurpCallbacks.getInstance().print("initAttackHttpMessage: changeValue: attack is null");
        }
        newHttpMessage.getReq().setChangeParam(changeParam);
        if (attack != null) {
            newHttpMessage.getReq().applyChangeParam();
        } else {
            //BurpCallbacks.getInstance().print("initAttackHttpMessage: ApplyChange: attack is null");
        }
        
        // Set parent
        newHttpMessage.setParentHttpMessage(initialMessage);
        
        // Apply new session
        if (mainSessionName != null) {
            if (! mainSessionName.equals("<default>") && ! mainSessionName.startsWith("<")) {
                String sessionVarName = SessionManager.getInstance().getSessionVarName();
                String sessionVarValue = SessionManager.getInstance().getValueFor(mainSessionName);

                // Dont do it if we already modified the session parameter
                if (!sessionVarName.equals(changeParam.getName())) {
//                BurpCallbacks.getInstance().print("Change session: " + sessionVarName + " " + sessionVarValue);
                    newHttpMessage.getReq().changeSession(sessionVarName, sessionVarValue);
                }
            }
        }
        
        //BurpCallbacks.getInstance().print("\n\nAfter: \n" + newHttpMessage.getReq().getRequestStr());
        return newHttpMessage;
    }
    
}