package com.example.shokacshoes;

public class ShoesOffset {
    double Offset_1_Fx = 0;
    double Offset_1_Fy = 0;
    double Offset_1_Fz = 0;
    double Offset_1_Mx = 0;
    double Offset_1_My = 0;
    double Offset_1_Mz = 0;
    double Offset_2_Fx = 0;
    double Offset_2_Fy = 0;
    double Offset_2_Fz = 0;
    double Offset_2_Mx = 0;
    double Offset_2_My = 0;
    double Offset_2_Mz = 0;
    double Offset_3_Fx = 0;
    double Offset_3_Fy = 0;
    double Offset_3_Fz = 0;
    double Offset_3_Mx = 0;
    double Offset_3_My = 0;
    double Offset_3_Mz = 0;


    public ShoesOffset(Sensor6axis sens,int start,int end){


        Offset_1_Fx = sens.sensor1.getAverageFx(start, end);
        Offset_1_Fy = sens.sensor1.getAverageFy(start, end);
        Offset_1_Fz = sens.sensor1.getAverageFz(start, end);
        Offset_1_Mx = sens.sensor1.getAverageMx(start, end);
        Offset_1_My = sens.sensor1.getAverageMy(start, end);
        Offset_1_Mz = sens.sensor1.getAverageMz(start, end);

        Offset_2_Fx = sens.sensor2.getAverageFx(start, end);
        Offset_2_Fy = sens.sensor2.getAverageFy(start, end);
        Offset_2_Fz = sens.sensor2.getAverageFz(start, end);
        Offset_2_Mx = sens.sensor2.getAverageMx(start, end);
        Offset_2_My = sens.sensor2.getAverageMy(start, end);
        Offset_2_Mz = sens.sensor2.getAverageMz(start, end);

        Offset_3_Fx = sens.sensor3.getAverageFx(start, end);
        Offset_3_Fy = sens.sensor3.getAverageFy(start, end);
        Offset_3_Fz = sens.sensor3.getAverageFz(start, end);
        Offset_3_Mx = sens.sensor3.getAverageMx(start, end);
        Offset_3_My = sens.sensor3.getAverageMy(start, end);
        Offset_3_Mz = sens.sensor3.getAverageMz(start, end);

    }

}


