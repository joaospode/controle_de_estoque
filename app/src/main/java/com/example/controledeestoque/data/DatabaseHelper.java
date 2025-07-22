package com.example.controledeestoque.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.controledeestoque.model.Funcionario;
import com.example.controledeestoque.model.Uniforme;
import com.example.controledeestoque.model.HistoricoEntrega;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "uniformes.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabela Setores
        db.execSQL("CREATE TABLE setores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL UNIQUE" +
                ");");

        // Tabela Funções
        db.execSQL("CREATE TABLE funcoes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "setor_id INTEGER NOT NULL, " +
                "FOREIGN KEY(setor_id) REFERENCES setores(id)" +
                ");");

        // Tabela Funcionários
        db.execSQL("CREATE TABLE funcionarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "data_nascimento TEXT, " +
                "cpf TEXT UNIQUE, " +
                "setor TEXT, " +
                "funcao TEXT, " +
                "data_admissao TEXT" +
                ");");

        // Tabela Uniformes
        db.execSQL("CREATE TABLE uniformes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tipo TEXT NOT NULL, " +
                "ca TEXT" +
                ");");

        // Tabela Estoque
        db.execSQL("CREATE TABLE estoque (" +
                "uniforme_id INTEGER PRIMARY KEY, " +
                "quantidade INTEGER, " +
                "FOREIGN KEY(uniforme_id) REFERENCES uniformes(id)" +
                ");");

        // Tabela Entregas
        db.execSQL("CREATE TABLE entregas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "funcionario_id INTEGER, " +
                "uniforme_id INTEGER, " +
                "quantidade INTEGER, " +
                "data_entrega TEXT, " +
                "FOREIGN KEY(funcionario_id) REFERENCES funcionarios(id), " +
                "FOREIGN KEY(uniforme_id) REFERENCES uniformes(id)" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS entregas;");
        db.execSQL("DROP TABLE IF EXISTS estoque;");
        db.execSQL("DROP TABLE IF EXISTS uniformes;");
        db.execSQL("DROP TABLE IF EXISTS funcionarios;");
        db.execSQL("DROP TABLE IF EXISTS funcoes;");
        db.execSQL("DROP TABLE IF EXISTS setores;");
        onCreate(db);
    }

    // --- Métodos para Setores e Funções ---

    public boolean insertSetor(String nome) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT id FROM setores WHERE nome = ?", new String[]{nome});
        boolean existe = c.moveToFirst();
        c.close();
        if (existe) return true;
        ContentValues cv = new ContentValues();
        cv.put("nome", nome);
        long id = db.insert("setores", null, cv);
        return id != -1;
    }

    public long getSetorIdByNome(String nome) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id FROM setores WHERE nome = ?", new String[]{nome});
        long id = -1;
        if (c.moveToFirst()) {
            id = c.getLong(0);
        }
        c.close();
        return id;
    }

    public boolean insertFuncao(String nomeFuncao, int setorId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nome", nomeFuncao);
        cv.put("setor_id", setorId);
        long id = db.insert("funcoes", null, cv);
        return id != -1;
    }

    public List<String> getAllSetores() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT nome FROM setores ORDER BY nome", null);
        while (c.moveToNext()) {
            list.add(c.getString(0));
        }
        c.close();
        return list;
    }

    public List<String> getFuncoesBySetor(String nomeSetor) {
        List<String> list = new ArrayList<>();
        long setorId = getSetorIdByNome(nomeSetor);
        if (setorId == -1) return list;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT nome FROM funcoes WHERE setor_id = ? ORDER BY nome",
                new String[]{String.valueOf(setorId)}
        );
        while (c.moveToNext()) {
            list.add(c.getString(0));
        }
        c.close();
        return list;
    }

    // --- Métodos Funcionario ---

    public long insertFuncionario(Funcionario f) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nome", f.getNome());
        cv.put("data_nascimento", f.getDataNascimento());
        cv.put("cpf", f.getCpf());
        cv.put("setor", f.getSetor());
        cv.put("funcao", f.getFuncao());
        cv.put("data_admissao", f.getDataAdmissao());
        return db.insert("funcionarios", null, cv);
    }

    public int deleteFuncionarioByCpf(String cpf) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("funcionarios", "cpf = ?", new String[]{cpf});
    }

    public List<Funcionario> getAllFuncionarios() {
        List<Funcionario> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id, nome FROM funcionarios ORDER BY nome", null);
        while (c.moveToNext()) {
            Funcionario f = new Funcionario();
            f.setId(c.getInt(0));
            f.setNome(c.getString(1));
            list.add(f);
        }
        c.close();
        return list;
    }

    // --- Métodos Uniforme ---

    public long insertUniforme(Uniforme u) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tipo", u.getTipo());
        cv.put("ca", u.getCa());
        return db.insert("uniformes", null, cv);
    }

    public int deleteUniformeByTipo(String tipo) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("uniformes", "tipo = ?", new String[]{tipo});
    }

    public List<Uniforme> getAllUniformes() {
        List<Uniforme> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id, tipo FROM uniformes ORDER BY tipo", null);
        while (c.moveToNext()) {
            Uniforme u = new Uniforme();
            u.setId(c.getInt(0));
            u.setTipo(c.getString(1));
            list.add(u);
        }
        c.close();
        return list;
    }

    // --- Métodos Estoque ---

    public boolean addStock(int uniformeId, int quantidade) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT quantidade FROM estoque WHERE uniforme_id = ?", new String[]{String.valueOf(uniformeId)});
        int current = 0;
        if (c.moveToFirst()) {
            current = c.getInt(0);
            c.close();
            ContentValues cv = new ContentValues();
            cv.put("quantidade", current + quantidade);
            return db.update("estoque", cv, "uniforme_id = ?", new String[]{String.valueOf(uniformeId)}) > 0;
        } else {
            c.close();
            ContentValues cv = new ContentValues();
            cv.put("uniforme_id", uniformeId);
            cv.put("quantidade", quantidade);
            long id = db.insert("estoque", null, cv);
            return id != -1;
        }
    }

    public boolean removeStock(int uniformeId, int quantidade) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT quantidade FROM estoque WHERE uniforme_id = ?", new String[]{String.valueOf(uniformeId)});
        if (!c.moveToFirst()) {
            c.close();
            return false;
        }
        int current = c.getInt(0);
        c.close();
        if (current < quantidade) return false;
        ContentValues cv = new ContentValues();
        cv.put("quantidade", current - quantidade);
        return db.update("estoque", cv, "uniforme_id = ?", new String[]{String.valueOf(uniformeId)}) > 0;
    }

    public List<Uniforme> getAllUniformesComEstoque() {
        List<Uniforme> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT u.id, u.tipo, u.ca, IFNULL(e.quantidade,0) as quantidadeEstoque FROM uniformes u LEFT JOIN estoque e ON u.id=e.uniforme_id", null);
        while (c.moveToNext()) {
            Uniforme u = new Uniforme();
            u.setId(c.getInt(0));
            u.setTipo(c.getString(1));
            u.setCa(c.getString(2));
            u.setQuantidadeEstoque(c.getInt(3));
            list.add(u);
        }
        c.close();
        return list;
    }

    // --- Métodos Entrega e Histórico ---

    public long insertEntrega(int funcionarioId, int uniformeId, int quantidade, String dataEntrega) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        long entregaId = -1;
        try {
            ContentValues cv = new ContentValues();
            cv.put("funcionario_id", funcionarioId);
            cv.put("uniforme_id", uniformeId);
            cv.put("quantidade", quantidade);
            cv.put("data_entrega", dataEntrega);
            entregaId = db.insert("entregas", null, cv);
            if (entregaId == -1) throw new RuntimeException("Falha ao inserir entrega");

            Cursor c = db.rawQuery("SELECT quantidade FROM estoque WHERE uniforme_id = ?", new String[]{String.valueOf(uniformeId)});
            int current = 0;
            if (c.moveToFirst()) current = c.getInt(0);
            c.close();
            if (current < quantidade) throw new RuntimeException("Estoque insuficiente");

            ContentValues stockCv = new ContentValues();
            stockCv.put("quantidade", current - quantidade);
            db.update("estoque", stockCv, "uniforme_id = ?", new String[]{String.valueOf(uniformeId)});

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return entregaId;
    }

    public List<HistoricoEntrega> getAllDeliveries() {
        return getDeliveriesByFilter(null, null);
    }

    public List<HistoricoEntrega> getDeliveriesByFilter(String funcFilter, String uniFilter) {
        List<HistoricoEntrega> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String funcLike = funcFilter == null || funcFilter.isEmpty() ? "%" : "%" + funcFilter + "%";
        String uniLike = uniFilter == null || uniFilter.isEmpty() ? "%" : "%" + uniFilter + "%";
        Cursor c = db.rawQuery("SELECT e.id, f.nome as funcionario, u.tipo as uniforme, e.quantidade, e.data_entrega " +
                "FROM entregas e " +
                "JOIN funcionarios f ON e.funcionario_id=f.id " +
                "JOIN uniformes u ON e.uniforme_id=u.id " +
                "WHERE f.nome LIKE ? AND u.tipo LIKE ? " +
                "ORDER BY e.data_entrega DESC", new String[]{funcLike, uniLike});
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String fn = c.getString(1);
            String ut = c.getString(2);
            int qt = c.getInt(3);
            String date = c.getString(4);
            list.add(new HistoricoEntrega(id, fn, ut, qt, date));
        }
        c.close();
        return list;
    }

    /** Remove funcionário pelo ID. Retorna número de linhas afetadas. */
    public int deleteFuncionarioById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("funcionarios", "id = ?", new String[]{ String.valueOf(id) });
    }

    /** Remove uniforme pelo ID. Retorna número de linhas afetadas. */
    public int deleteUniformeById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("uniformes", "id = ?", new String[]{ String.valueOf(id) });
    }

    public List<Funcionario> getAllFuncionariosDetalhado() {
        List<Funcionario> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT id, nome, setor, funcao, data_admissao FROM funcionarios ORDER BY nome",
                null
        );
        while (c.moveToNext()) {
            Funcionario f = new Funcionario();
            f.setId(c.getInt(c.getColumnIndexOrThrow("id")));
            f.setNome(c.getString(c.getColumnIndexOrThrow("nome")));
            f.setSetor(c.getString(c.getColumnIndexOrThrow("setor")));
            f.setFuncao(c.getString(c.getColumnIndexOrThrow("funcao")));
            f.setDataAdmissao(c.getString(c.getColumnIndexOrThrow("data_admissao")));
            list.add(f);
        }
        c.close();
        return list;
    }
}