import java.util.ArrayList;
import java.util.List;

/**
 * Class représentant un film
 * @author Kévin
 *
 */
public class Film {
	
	private String name;
	
	private String duree;
	
	private String rated;
	
	private Cinema cinema;
	
	private List<Seance> seanceListVF;
	
	private List<Seance> seanceListVOSTFR;

	public Film(String name, String duree, String rated, Cinema cinema) {
		super();
		this.name = name;
		this.duree = duree;
		this.rated = rated;
		this.seanceListVF = new ArrayList<Seance>();
		this.seanceListVOSTFR = new ArrayList<>();
	}
	
	public void addSeanceVF(Seance seance) {
		this.seanceListVF.add(seance);
	}
	
	public void addSeanceVOSTFR(Seance seance) {
		this.seanceListVOSTFR.add(seance);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDuree() {
		return duree;
	}

	public void setDuree(String duree) {
		this.duree = duree;
	}

	public String getRated() {
		return rated;
	}

	public void setRated(String rated) {
		this.rated = rated;
	}

	public List<Seance> getSeanceListVF() {
		return seanceListVF;
	}

	public void setSeanceListVF(List<Seance> seanceListVF) {
		this.seanceListVF = seanceListVF;
	}

	public List<Seance> getSeanceListVOSTFR() {
		return seanceListVOSTFR;
	}

	public void setSeanceListVOSTFR(List<Seance> seanceListVOSTFR) {
		this.seanceListVOSTFR = seanceListVOSTFR;
	}

	@Override
	public String toString() {
		return "Film [name=" + name + ", duree=" + duree + ", rated=" + rated + ", seanceListVF="
				+ seanceListVF + ", seanceListVOSTFR=" + seanceListVOSTFR + "]";
	}


	
	

}
