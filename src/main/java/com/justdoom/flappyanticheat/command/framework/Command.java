package com.justdoom.flappyanticheat.command.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
  String name();

  String permission() default "";

  String[] aliases() default {};
  
  String description() default "";
  
  String usage() default "";
  
  boolean inGameOnly() default true;
}


/* Location:              C:\Users\enyar\AppData\Local\Temp\BNZ.612828ac4818faa2\LordMeme-1.0-SNAPSHOT.jar\\us\zonix\antichea\\util\command\Command.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */