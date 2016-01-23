package hr.fer.zemris.sm.game.controllers;

/**
 * Every controller that implements this interface should provide
 * some kind of method of connecting to an element that it controls.
 *
 * Created by Fredi Šarić on 11.01.16.
 */
public interface IConnectibleController extends IController {

    public void connect();

}
