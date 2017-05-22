package net.bdew.wurm.privacy;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmClientMod;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PrivacyMod implements WurmClientMod, Initable, PreInitable {
    private static final Logger logger = Logger.getLogger("PrivacyMod");

    public static void logException(String msg, Throwable e) {
        if (logger != null)
            logger.log(Level.SEVERE, msg, e);
    }

    public static void logInfo(String msg) {
        if (logger != null)
            logger.log(Level.INFO, msg);
    }


    @Override
    public void preInit() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            CtClass ctSSC = classPool.getCtClass("com.wurmonline.client.comm.SimpleServerConnectionClass");
            CtClass ctBB = classPool.getCtClass("java.nio.ByteBuffer");

            ctSSC.getMethod("reallyHandleCmdLogin", javassist.bytecode.Descriptor.ofMethod(CtClass.voidType, new CtClass[]{ctBB}))
                    .insertAfter(
                            "com.wurmonline.client.WurmClientBase.steamHandler.cancelAuthTicket();" +
                                    "net.bdew.wurm.privacy.PrivacyMod.logInfo(\"Login complete, dropping auth ticket\");"
                    );

        } catch (NotFoundException | CannotCompileException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() {
    }
}
