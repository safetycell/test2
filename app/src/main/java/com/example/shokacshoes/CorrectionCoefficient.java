package com.example.shokacshoes;


import org.apache.commons.math3.exception.OutOfRangeException;

import java.io.Serializable;

public class CorrectionCoefficient implements Serializable {
    public double Left_AI_1 = 0;
    public double Left_AI_2 = 0;
    public double Left_AI_3 = 0;
    public double Right_AI_1 = 0;
    public double Right_AI_2 = 0;
    public double Right_AI_3 = 0;
    double Left_Offset_1_Fx = 0;
    double Left_Offset_1_Fy = 0;
    double Left_Offset_1_Fz = 0;
    double Left_Offset_1_Mx = 0;
    double Left_Offset_1_My = 0;
    double Left_Offset_1_Mz = 0;
    double Left_Offset_2_Fx = 0;
    double Left_Offset_2_Fy = 0;
    double Left_Offset_2_Fz = 0;
    double Left_Offset_2_Mx = 0;
    double Left_Offset_2_My = 0;
    double Left_Offset_2_Mz = 0;
    double Left_Offset_3_Fx = 0;
    double Left_Offset_3_Fy = 0;
    double Left_Offset_3_Fz = 0;
    double Left_Offset_3_Mx = 0;
    double Left_Offset_3_My = 0;
    double Left_Offset_3_Mz = 0;

    double Right_Offset_1_Fx = 0;
    double Right_Offset_1_Fy = 0;
    double Right_Offset_1_Fz = 0;
    double Right_Offset_1_Mx = 0;
    double Right_Offset_1_My = 0;
    double Right_Offset_1_Mz = 0;
    double Right_Offset_2_Fx = 0;
    double Right_Offset_2_Fy = 0;
    double Right_Offset_2_Fz = 0;
    double Right_Offset_2_Mx = 0;
    double Right_Offset_2_My = 0;
    double Right_Offset_2_Mz = 0;
    double Right_Offset_3_Fx = 0;
    double Right_Offset_3_Fy = 0;
    double Right_Offset_3_Fz = 0;
    double Right_Offset_3_Mx = 0;
    double Right_Offset_3_My = 0;
    double Right_Offset_3_Mz = 0;
    public CorrectionCoefficient(){

    }
    public CorrectionCoefficient(ShoesOffset left_o,Calibration left_c,ShoesOffset right_o,Calibration right_c){
        try {
            Left_Offset_1_Fx = left_o.Offset_1_Fx;
            Left_Offset_1_Fy = left_o.Offset_1_Fy;
            Left_Offset_1_Fz = left_o.Offset_1_Fz;
            Left_Offset_1_Mx = left_o.Offset_1_Mx;
            Left_Offset_1_My = left_o.Offset_1_My;
            Left_Offset_1_Mz = left_o.Offset_1_Mz;

            Left_Offset_2_Fx = left_o.Offset_2_Fx;
            Left_Offset_2_Fy = left_o.Offset_2_Fy;
            Left_Offset_2_Fz = left_o.Offset_2_Fz;
            Left_Offset_2_Mx = left_o.Offset_2_Mx;
            Left_Offset_2_My = left_o.Offset_2_Mx;
            Left_Offset_2_Mz = left_o.Offset_2_Mz;

            Left_Offset_3_Fx = left_o.Offset_3_Fx;
            Left_Offset_3_Fy = left_o.Offset_3_Fy;
            Left_Offset_3_Fz = left_o.Offset_3_Fz;
            Left_Offset_3_Mx = left_o.Offset_3_Mx;
            Left_Offset_3_My = left_o.Offset_3_Mx;
            Left_Offset_3_Mz = left_o.Offset_3_Mz;

            Left_AI_1 = left_c.Cefficients.getEntry(0, 0);
            Left_AI_2 = left_c.Cefficients.getEntry(0, 1);
            Left_AI_3 = left_c.Cefficients.getEntry(0, 2);


            Right_Offset_1_Fx = right_o.Offset_1_Fx;
            Right_Offset_1_Fy = right_o.Offset_1_Fy;
            Right_Offset_1_Fz = right_o.Offset_1_Fz;
            Right_Offset_1_Mx = right_o.Offset_1_Mx;
            Right_Offset_1_My = right_o.Offset_1_My;
            Right_Offset_1_Mz = right_o.Offset_1_Mz;

            Right_Offset_2_Fx = right_o.Offset_2_Fx;
            Right_Offset_2_Fy = right_o.Offset_2_Fy;
            Right_Offset_2_Fz = right_o.Offset_2_Fz;
            Right_Offset_2_Mx = right_o.Offset_2_Mx;
            Right_Offset_2_My = right_o.Offset_2_Mx;
            Right_Offset_2_Mz = right_o.Offset_2_Mz;

            Right_Offset_3_Fx = right_o.Offset_3_Fx;
            Right_Offset_3_Fy = right_o.Offset_3_Fy;
            Right_Offset_3_Fz = right_o.Offset_3_Fz;
            Right_Offset_3_Mx = right_o.Offset_3_Mx;
            Right_Offset_3_My = right_o.Offset_3_Mx;
            Right_Offset_3_Mz = right_o.Offset_3_Mz;

            Right_AI_1 = right_c.Cefficients.getEntry(0, 0);
            Right_AI_2 = right_c.Cefficients.getEntry(0, 1);
            Right_AI_3 = right_c.Cefficients.getEntry(0, 2);


        } catch (OutOfRangeException e) {
            e.printStackTrace();
        }

    }

}
