package com.sahib.uberApp.UberApplication.utils;

import com.sahib.uberApp.UberApplication.dtos.PointDto;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtil {

    public static Point convertPointDtoToPoint(PointDto pointDto){
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

        Coordinate coordinate = new Coordinate(
                pointDto.getCoordinates()[0], // lon
                pointDto.getCoordinates()[1]  // lat
        );

        return geometryFactory.createPoint(coordinate);
    }


}
