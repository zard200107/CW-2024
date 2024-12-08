package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.example.demo.LevelParent;
import com.example.demo.controller.ResourceManager;

public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.model.level.LevelOne";
	private final Stage stage;
	// 当前关卡引用
	private LevelParent currentLevel;
	// 转换状态标志
	private boolean isTransitioning = false;

	public Controller(Stage stage) {
		this.stage = stage;
	}

	public void launchGame() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// 预加载所有游戏图片资源
		ResourceManager.preloadResources();

		stage.show();
		goToLevel(LEVEL_ONE_CLASS_NAME);
	}

	private void goToLevel(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// 清理旧关卡
		if (currentLevel != null) {
			currentLevel.deleteObserver(this);
			// 停止旧关卡的所有活动
			currentLevel.cleanup();
		}

		// 加载新关卡
		Class<?> myClass = Class.forName(className);
		Constructor<?> constructor = myClass.getConstructor(double.class, double.class);
		LevelParent myLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
		myLevel.addObserver(this);

		// 初始化和启动新关卡
		Scene scene = myLevel.initializeScene();
		stage.setScene(scene);
		myLevel.startGame();

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// 如果正在转换，则不进行操作
		if (isTransitioning)
			return;

		try {
			isTransitioning = true;
			goToLevel((String) arg1);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e.getClass().toString());
			alert.show();
		} finally {
			isTransitioning = false;
		}
	}

}
