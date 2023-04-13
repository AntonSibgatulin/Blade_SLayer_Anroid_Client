package ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader;

public interface IPlayer {
    public void run();
    public void jump();
    public void breath(int type);
    public void style(EBreath style);
    public void sitdown();
    public EPlayer getType();

}
