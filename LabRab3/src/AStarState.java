import java.util.HashMap;

public class AStarState
{

    private Map2D map;

    //добавила новые поля для открытых и закрытых вершин
    //локация-ключи,вершины-значения
    HashMap<Location, Waypoint> OpenedWaypoint;
    HashMap<Location, Waypoint> ClosedWaypoint;

    //добавила инициализацию новх полей
    public AStarState(Map2D map)
    {
        if (map == null) {
            throw new NullPointerException("map cannot be null");
        }
        else {
            this.OpenedWaypoint = new HashMap();
            this.ClosedWaypoint = new HashMap();
            this.map = map;
        }
    }

    public Map2D getMap()
    {
        return map;
    }


    public Waypoint getMinOpenWaypoint()
    //проверяет все вершины в наборе открытых вершин,
    //возвращает ссылку на вершину с наименьшей общей стоимостью
    {
        Waypoint[] MinOpenWaypoint = new Waypoint[1];
        float[] j = new float[]{3.4028235E38F};

        this.OpenedWaypoint.forEach((k, v) -> {//перебираем все открытые вершины
            //ищем наименьшую стоимость
            if (v.getTotalCost() < j[0]) {
                MinOpenWaypoint[0] = v;
                j[0] = v.getTotalCost();
            }

        });

        return MinOpenWaypoint[0];
    }


    public boolean addOpenWaypoint(Waypoint newWP)
    //должен добавлять указанную вершину только в том
    //случае, если существующая вершина хуже новой
    {
        if (this.OpenedWaypoint.get(newWP.getLocation()) == null) {
            //если в открытых вершинах нет вершины с таким положением
            //добавляем новую вершину
            this.OpenedWaypoint.put(newWP.getLocation(), newWP);
        } else if (newWP.getPreviousCost() < ((Waypoint)this.OpenedWaypoint.get(newWP.getLocation())).getPreviousCost()) {
            //если стоимость пути до новой вершины меньше
            //добавляем новую вершину
            this.OpenedWaypoint.replace(newWP.getLocation(), newWP);
        }

        return false;
    }


    public int numOpenWaypoints() //возвращает количество точек в наборе открытых вершин
    {
        return this.OpenedWaypoint.values().size();
    }


    public void closeWaypoint(Location loc)
    //перемещает вершины из открытых в закрытые
    {
        this.OpenedWaypoint.remove(loc);//удаляем из открытых
        this.ClosedWaypoint.put(loc, this.OpenedWaypoint.get(loc));//добавляем в закрытые

    }


    public boolean isLocationClosed(Location loc)
    //проверяет есть ли указанное местоположение  в наборе закрытых вершин
    {
        return this.ClosedWaypoint.containsKey(loc);
    }
}
