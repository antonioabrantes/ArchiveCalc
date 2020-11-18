package mannings.msi.com.ttddtools;

public class TiposRegistros {
    private Integer id;
    private String nome;

    public TiposRegistros() {
    }

    public TiposRegistros(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getImagem() {
        switch (id) {
            case 0:
                return R.drawable.tipo1;
            case 1:
                return R.drawable.tipo2;
            case 2:
                return R.drawable.tipo3;
            case 3:
                return R.drawable.tipo4;
            case 4:
                return R.drawable.tipo5;
            case 5:
                return R.drawable.tipo6;

        }
        return R.drawable.tipo1;
    }
}