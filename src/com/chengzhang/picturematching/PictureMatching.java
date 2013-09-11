package com.chengzhang.picturematching;

import java.util.Timer;
import java.util.TimerTask;

import com.chengzhang.picturematching.R;
import com.chengzhang.picturematching.bom.LinkInfo;
import com.chengzhang.picturematching.bom.Piece;
import com.chengzhang.picturematching.common.GameConf;
import com.chengzhang.picturematching.common.GameService;
import com.chengzhang.picturematching.impl.GameEngine;
import com.chengzhang.picturematching.view.GameView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Jackie
 * 
 */
public class PictureMatching extends Activity {
	private GameConf _config;
	private GameService _gameService;
	private GameView _gameView;
	private Button _startButton;
	TextView _timeTextView;
	AlertDialog.Builder _lostDialog;
	private AlertDialog.Builder _successDialog;

	private Timer _timer = new Timer();
	int _gameTime;
	boolean _isPlaying;
	private Vibrator _vibrator;

	private Piece _selected = null;
	Handler _handler = new Handler() {
		public void handleMessage(Message mag) {
			switch (mag.what) {
			case 0x123:
				_timeTextView.setText("Time Left : " + _gameTime); //$NON-NLS-1$
				_gameTime--;
				if (_gameTime < 0) {
					stopTimer();
					_isPlaying = false;
					_lostDialog.show();
					return;
				}
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	}

	private void init() {
		_config = new GameConf(8, 9, 2, 10, 100000, this);
		_gameView = (GameView) findViewById(R.id.gameView);
		_timeTextView = (TextView) findViewById(R.id.timeText);
		_startButton = (Button) findViewById(R.id.startButton);
		_vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

		_gameService = new GameEngine(_config);
		_gameView.setGameService(_gameService);
		_lostDialog = createDialog("Lost", "Game Over", R.drawable.lost); //$NON-NLS-1$ //$NON-NLS-2$
		_successDialog = createDialog("Win", "You Win", R.drawable.success); //$NON-NLS-1$ //$NON-NLS-2$
		addListners();
	}

	private void addListners() {
		_startButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startGame(GameConf.DEFAULT_TIME);
			}
		});

		_gameView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					gameViewTouchDown(event);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					gameViewTouchUp(event);
				}
				return true;
			}
		});

		_lostDialog.setPositiveButton(
				"OK", new DialogInterface.OnClickListener() { //$NON-NLS-1$

					@Override
					public void onClick(DialogInterface dialog, int which) {
						startGame(GameConf.DEFAULT_TIME);

					}
				});

		_successDialog.setPositiveButton(
				"OK", new DialogInterface.OnClickListener() { //$NON-NLS-1$

					@Override
					public void onClick(DialogInterface dialog, int which) {
						startGame(GameConf.DEFAULT_TIME);

					}
				});

	}

	@Override
	protected void onPause() {
		stopTimer();
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (_isPlaying) {
			startGame(_gameTime);
		}
		super.onResume();
	}

	void gameViewTouchDown(MotionEvent event) {
		Piece[][] pieces = _gameService.getPieces();
		float touchX = event.getX();
		float touchY = event.getY();
		Piece selectedPiece = _gameService.findPiece(touchX, touchY);

		if (selectedPiece == null) {
			return;
		}

		_gameView.setSelectedPiece(selectedPiece);
		if (_selected == null) {
			_selected = selectedPiece;
			_gameView.postInvalidate();
			return;
		}
		LinkInfo linkInfo = _gameService.link(_selected, selectedPiece);
		if (linkInfo == null) {
			_selected = selectedPiece;
			_gameView.postInvalidate();
		} else {
			handleSuccessLink(linkInfo, _selected, selectedPiece, pieces);
		}
	}

	@SuppressWarnings("unused")
	void gameViewTouchUp(MotionEvent event) {
		_gameView.postInvalidate();
	}

	void startGame(int gameTime) {
		if (_timer != null) {
			stopTimer();
		}

		_gameTime = gameTime;
		if (_gameTime == GameConf.DEFAULT_TIME) {
			_gameView.startGame();
		}
		_isPlaying = true;
		_timer = new Timer();
		_timer.schedule(new TimerTask() {
			public void run() {
				_handler.sendEmptyMessage(0x123);
			}
		}, 0, 1000);
		_selected = null;
	}

	private void handleSuccessLink(LinkInfo linkInfo, Piece selected,
			Piece currentPiece, Piece[][] pieces) {
		_gameView.setLinkInfo(linkInfo);
		_gameView.setSelectedPiece(null);
		_gameView.postInvalidate();
		pieces[selected.getIndexX()][selected.getIndexY()] = null;
		pieces[currentPiece.getIndexX()][currentPiece.getIndexY()] = null;
		_selected = null;
		_vibrator.vibrate(100);
		if (!_gameService.hasPieces()) {
			_successDialog.show();
			stopTimer();
			_isPlaying = false;
		}
	}

	private AlertDialog.Builder createDialog(String title, String message,
			int imageResource) {
		return new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).setIcon(imageResource);
	}

	void stopTimer() {
		_timer.cancel();
		_timer = null;
	}

}
