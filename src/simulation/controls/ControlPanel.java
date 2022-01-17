package simulation.controls;

import simulation.constants.Constants;
import simulation.flightRoute.SetFlightRoute;
import simulation.globus.PointModelGlobe;
import simulation.globus.WireframeGlobe;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel implements ActionListener, ChangeListener {

    private JSlider speedAdjusting;
    private static final int minSpeed = -5;
    private static final int initSpeed = -3;
    private static final int maxSpeed = 5;
    private JButton switchView;
    private JTextField adjustS1;
    private JSpinner adjustAlpha;
    private PointModelGlobe pointModelGlobe;
    private WireframeGlobe wireframeGlobe;
    private SetFlightRoute setFlightRoute;


    public ControlPanel(PointModelGlobe pointModelGlobe, WireframeGlobe wireframeGlobe, SetFlightRoute setFlightRoute) {

        this.pointModelGlobe = pointModelGlobe;
        this.wireframeGlobe = wireframeGlobe;
        this.setFlightRoute = setFlightRoute;

        JFrame frame = new JFrame();

        //Create Slider:
        JLabel sliderTitle = new JLabel("Adjust rotation of globe");
        speedAdjusting = new JSlider(JSlider.HORIZONTAL, minSpeed, maxSpeed, initSpeed);
        speedAdjusting.addChangeListener(this);
        //Labels at major tick marks:
        speedAdjusting.setMajorTickSpacing(1);
        speedAdjusting.setMinorTickSpacing(1);
        speedAdjusting.setPaintTicks(true);
        speedAdjusting.setPaintLabels(true);

        //Create Button:
        JLabel buttonTitle = new JLabel("Set view on globe");
        switchView = new JButton("Bottom");
        switchView.addActionListener(this);

        //Create Spinner1:
        JLabel spinnerTitle1 = new JLabel("Adjust S1 of projection of globe");
        adjustS1 = new JTextField("0.63246");
        adjustS1.addActionListener(this);
        //Create Spinner2:
        JLabel spinnerTitle2 = new JLabel("Adjust Alpha of projection of globe");
        SpinnerModel numberField2 = new SpinnerNumberModel(135, 0, 360, 5);
        adjustAlpha = new JSpinner(numberField2);
        adjustAlpha.addChangeListener(this);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(2,2));
        panel.add(sliderTitle);
        panel.add(buttonTitle);
        panel.add(spinnerTitle1);
        panel.add(spinnerTitle2);
        panel.add(speedAdjusting);
        panel.add(switchView);
        panel.add(adjustS1);
        panel.add(adjustAlpha);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Control Panel");
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(!speedAdjusting.getValueIsAdjusting()) {
            Constants.rotateSpeed = computeRotateSpeed(speedAdjusting.getValue());
        }

        Constants.projectionAlpha = (int) adjustAlpha.getValue();
        Constants.resetPhiPThetaP();
        pointModelGlobe.resetProjectionMatrix();
        wireframeGlobe.resetProjectionMatrix();
        setFlightRoute.resetProjectionMatrix();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == switchView) {
            Constants.projectionS1 *= -1;
            Constants.resetPhiPThetaP();

            pointModelGlobe.resetProjectionMatrix();
            wireframeGlobe.resetProjectionMatrix();
            setFlightRoute.resetProjectionMatrix();

            if(switchView.getText() == "Bottom") {
                switchView.setText("Top");
            }
            else {
                switchView.setText("Bottom");
            }
        }
        if(e.getSource() == adjustS1) {
            Constants.projectionS1 = Double.parseDouble(adjustS1.getText());

            Constants.resetPhiPThetaP();
            pointModelGlobe.resetProjectionMatrix();
            wireframeGlobe.resetProjectionMatrix();
            setFlightRoute.resetProjectionMatrix();
        }
    }

    private double computeRotateSpeed(double sliderValue) {
        switch ((int) sliderValue) {
            case -1:
                return -0.1;
            case 1:
                return 0.1;
            case -2:
                return -0.2;
            case 2:
                return 0.2;
            case -3:
                return -0.3;
            case 3:
                return 0.3;
            case -4:
                return -0.4;
            case 4:
                return 0.4;
            case -5:
                return -0.5;
            case 5:
                return 0.5;
            default:
                return 0;
        }
    }
}
